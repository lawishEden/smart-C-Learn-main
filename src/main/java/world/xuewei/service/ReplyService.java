package world.xuewei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import world.xuewei.entity.Page;
import world.xuewei.entity.Reply;

import java.util.List;
import java.util.Map;

/**
* @author 26722
* @description 针对表【reply】的数据库操作Service
* @createDate 2024-03-14 10:35:01
*/
public interface ReplyService   extends IService<Reply> {



    /**
     * 根据帖子id查询帖子回复
     *
     * @param pid
     * @return
     */
    List<Reply> findReplyByPid(Long pid);


    /**
     * 保存回复
     *
     * @param reply
     */
    void saveReply(Reply reply);

    /**
     * 根据帖子id分页查询回复
     *
     * @return
     */
    Page<Reply> findReplyByPidAndPage(Map<String, Long> map);


    /**
     * 根据回复id删除回复
     *
     * @param rid
     */
    void deleteReplyRid(Long rid);

}
