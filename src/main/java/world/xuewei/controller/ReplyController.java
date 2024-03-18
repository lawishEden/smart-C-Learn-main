package world.xuewei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import world.xuewei.entity.User;
import world.xuewei.service.ReplyService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


@Controller
public class ReplyController {


    @Resource
    private ReplyService replyService;


    @RequestMapping(value = "/deleteReply/{rid}", method = RequestMethod.GET)
    @ResponseBody
    public String deleteReply(@PathVariable Long rid, HttpSession session) {

        User user = (User)session.getAttribute("user");
        if (null != user && user.getLevel() == 0) {
            replyService.deleteReplyRid(rid);
            return "删除成功";
        }
        return "能不能删你心里没B数？";
    }

}
