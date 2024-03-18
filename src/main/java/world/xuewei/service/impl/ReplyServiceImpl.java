package world.xuewei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import world.xuewei.dao.PostMapper;
import world.xuewei.entity.Post;
import world.xuewei.entity.Reply;
import world.xuewei.service.ReplyService;
import world.xuewei.dao.ReplyMapper;
import world.xuewei.entity.Page;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 26722
* @description 针对表【reply】的数据库操作Service实现
* @createDate 2024-03-14 10:35:01
*/
@Service
@Transactional
public class ReplyServiceImpl  extends ServiceImpl<ReplyMapper, Reply>
    implements ReplyService{

    @Resource
    private ReplyMapper replyMapper;


    @Resource
    @Lazy
    private PostMapper postMapper;


    @Override
    public List<Reply> findReplyByPid(Long pid) {

        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid);


        return replyMapper.selectList(queryWrapper);
    }

    @Override
    public void saveReply(Reply reply) {

        Post post = reply.getPost();
        Date lastreplytime = post.getLastreplytime();
        UpdateWrapper<Post> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("lastreplytime", lastreplytime).eq("pid",post.getPid());



        postMapper.update(null,updateWrapper);



        replyMapper.insert(reply);
    }

    @Override
    public Page<Reply> findReplyByPidAndPage(Map<String, Long> map) {
        return null;
    }

    @Override
    public void deleteReplyRid(Long rid) {

    }
}




