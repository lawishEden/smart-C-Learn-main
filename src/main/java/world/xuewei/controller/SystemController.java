package world.xuewei.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import world.xuewei.constant.MedicalConstants;
import world.xuewei.entity.*;
import world.xuewei.service.PostService;
import world.xuewei.service.ReplyService;
import world.xuewei.service.UserService;
import world.xuewei.utils.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 系统跳转控制器
 * <p>
 * ==========================================================================
 * 郑重说明：本项目免费开源！原创作者为：薛伟同学，严禁私自出售。
 * ==========================================================================
 * B站账号：薛伟同学
 * 微信公众号：薛伟同学
 * 作者博客：http://xuewei.world
 * ==========================================================================
 * 陆陆续续总会收到粉丝的提醒，总会有些人为了赚取利益倒卖我的开源项目。
 * 不乏有粉丝朋友出现钱付过去，那边只把代码发给他就跑路的，最后还是根据线索找到我。。
 * 希望各位朋友擦亮慧眼，谨防上当受骗！
 * ==========================================================================
 *
 * @author <a href="http://xuewei.world/about">XUEW</a>
 */
@Controller
public class SystemController extends BaseController<User> {

    @Resource
    private UserService userService;

    @Resource
    private ReplyService replyService;

    /**
     * 首页
     */
    @GetMapping("/index.html")
    public String index(Map<String, Object> map) {
        return "index";
    }

    /**
     * 智能医生
     */
    @GetMapping("/doctor")
    public String doctor(Map<String, Object> map) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }
        return "doctor";
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/index.html";
    }

    /**
     * 所有反馈
     */
    @GetMapping("/all-feedback")
    public String feedback(Map<String, Object> map) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }
        List<Feedback> feedbackList = feedbackService.all();

        map.put("feedbackList", feedbackList);
        return "all-feedback";
    }

    /**
     * 我的资料
     */
    @GetMapping("/profile")
    public String profile(Map<String, Object> map) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }
        return "profile";
    }

    /**
     * 查询相关疾病
     */
    @GetMapping("findIllness")
    public String findIllness(Map<String, Object> map, Integer kind, String illnessName, Integer page) {
        // 处理page
        page = ObjectUtils.isEmpty(page) ? 1 : page;

        Map<String, Object> illness = illnessService.findIllness(kind, illnessName, page);
        if (Assert.notEmpty(kind)) {
            map.put("title", illnessKindService.get(kind).getName() + (illnessName == null ? "" : ('"' + illnessName + '"' + "的搜索结果")));
        } else {
            map.put("title", illnessName == null ? "全部" : ('"' + illnessName + '"' + "的搜索结果"));
        }
        if (loginUser != null && kind != null) {
            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_OPERATE,
                    illnessKindService.get(kind).getId() + "," + (Assert.isEmpty(illnessName) ? "无" : illnessName));
        }
        if (loginUser != null && Assert.notEmpty(illnessName)) {
            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_ILLNESS, illnessName);
        }
        map.putAll(illness);
        map.put("page", page);
        map.put("kind", kind);
        map.put("illnessName", illnessName);
        map.put("kindList", illnessKindService.findList());
        map.put("history", loginUser == null ? null : historyService.findList(loginUser.getId()));
        return "search-illness";
    }

    /**
     * 查询相关疾病下的药
     */
    @GetMapping("findIllnessOne")
    public String findIllnessOne(Map<String, Object> map, Integer id) {
        Map<String, Object> illnessOne = illnessService.findIllnessOne(id);
        Illness illness = illnessService.get(id);
        if (loginUser != null) {
            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_ILLNESS, illness.getIllnessName());
        }
        map.putAll(illnessOne);
        return "illness-reviews";
    }

    /**
     * 查询相关疾病下的药
     */
    @GetMapping("findMedicineOne")
    public String findMedicineOne(Map<String, Object> map, Integer id) {
        Medicine medicine = medicineService.get(id);
//        historyService.insetOne(loginUser.getId(),MedicalConstants.TYPE_MEDICINE,medicine.getMedicineName());
        map.put("medicine", medicine);
        return "medicine";
    }

    /**
     * 查询相关疾病下的药
     */
    @GetMapping("findMedicines")
    public String findMedicines(Map<String, Object> map, String nameValue, Integer page) {
        // 处理page
        page = ObjectUtils.isEmpty(page) ? 1 : page;
        if (loginUser != null && Assert.notEmpty(nameValue)) {
            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_MEDICINE, nameValue);
        }
        map.putAll(medicineService.getMedicineList(nameValue, page));
        map.put("history", loginUser == null ? null : historyService.findList(loginUser.getId()));
        map.put("title", nameValue);
        return "illness";
    }

    /**
     * 查询相关疾病下的药
     */
    @GetMapping("globalSelect")
    public String globalSelect(Map<String, Object> map, String nameValue) {
        nameValue = nameValue.replace("，", ",");
        List<String> idArr = Arrays.asList(nameValue.split(","));
        //首先根据关键字去查询
        Set<Illness> illnessSet = new HashSet<>();
        idArr.forEach(s -> {
            Illness one = illnessService.getOne(new QueryWrapper<Illness>().like("illness_name", s));
            if (ObjectUtil.isNotNull(one)) {
                illnessSet.add(one);
            }
        });
        idArr.forEach(s -> {
            Illness one = illnessService.getOne(new QueryWrapper<Illness>().like("special_symptom", s));
            if (ObjectUtil.isNotNull(one)) {
                illnessSet.add(one);
            }
        });
        idArr.forEach(s -> {
            Illness one = illnessService.getOne(new QueryWrapper<Illness>().like("illness_symptom", s));
            if (ObjectUtil.isNotNull(one)) {
                illnessSet.add(one);
            }
        });
        map.put("illnessSet", illnessSet);
        return "index";
    }

    /**
     * 添加疾病页面
     */
    @GetMapping("add-illness")
    public String addIllness(Integer id, Map<String, Object> map) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }
        Illness illness = new Illness();
        if (Assert.notEmpty(id)) {
            illness = illnessService.get(id);
        }
        List<IllnessKind> illnessKinds = illnessKindService.all();
        map.put("illness", illness);
        map.put("kinds", illnessKinds);
        return "add-illness";
    }

    /**
     * 添加药品页面
     */
    @GetMapping("add-medical")
    public String addMedical(Integer id, Map<String, Object> map) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }
        List<Illness> illnesses = illnessService.all();
        Medicine medicine = new Medicine();
        if (Assert.notEmpty(id)) {
            medicine = medicineService.get(id);
            for (Illness illness : illnesses) {
                List<IllnessMedicine> query = illnessMedicineService.query(IllnessMedicine.builder().medicineId(id).illnessId(illness.getId()).build());
                if (Assert.notEmpty(query)) {
                    illness.setIllnessMedicine(query.get(0));
                }
            }
        }
        map.put("illnesses", illnesses);
        map.put("medicine", medicine);
        return "add-medical";
    }

    /**
     * 疾病管理页面
     */
    @GetMapping("all-illness")
    public String allIllness(Map<String, Object> map) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }
        List<Illness> illnesses = illnessService.all();
        for (Illness illness : illnesses) {
            illness.setKind(illnessKindService.get(illness.getKindId()));
        }
        map.put("illnesses", illnesses);
        return "all-illness";
    }

    /**
     * 药品管理页面
     */
    @GetMapping("all-medical")
    public String allMedical(Map<String, Object> map) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }
        List<Medicine> medicines = medicineService.all();
        map.put("medicines", medicines);
        return "all-medical";
    }
    /**
     * 论坛页面
     */
//    @Autowired
//    private PostService postService;
//    @GetMapping("PostIndex")
//    public String PostIndex(HttpSession session, Model model, @RequestParam(value = "page", required = false) Long page) {
//        if (Assert.isEmpty(loginUser)) {
//            Map<String, Long> map = new HashMap<>();
//            map.put("startPage", page == null ? 0 : page - 1);
//            model.addAttribute("page", postService.findPostByPage(map));
//            return "PostIndex";
//        }
//        return "index";
//    }

    /**
     * 在线编程
     */
    @GetMapping("online")
    public String online(Map<String, Object> map) {
        return "online";
    }


   @Resource
    private PostService postService;

    @GetMapping("/PostIndex")
    public String index(HttpSession session, Model model, @RequestParam(value = "page", required = false) Long page) {
        if (Assert.isEmpty(loginUser)) {
            return "redirect:/index.html";
        }

        Long currentPage = page == null ? 1 : page;


        Page<Post> objectPage = new Page<>(currentPage, 5);
        List<Post> records = objectPage.getRecords();
        if (records.size() == 0){


            objectPage.setRecords(new ArrayList<>());
        }


       Page<Post> page1 = postService.page(objectPage);

        List<Post> records1 = page1.getRecords();
        records1.stream().forEach(post -> {
            Integer userId = post.getId();
            User user = userService.get(userId);
            post.setUser(user);

            QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pid", post.getPid());

            long count = replyService.count(queryWrapper);
            post.setReplyCount(count);


        });


        model.addAttribute("page", page1);
        return "PostIndex";


  }
}
