package world.xuewei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import world.xuewei.dao.PostMapper;
import world.xuewei.dao.ReplyMapper;
import world.xuewei.entity.Post;
import world.xuewei.entity.Reply;
import world.xuewei.service.PostService;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 26722
* @description 针对表【post】的数据库操作Service实现
* @createDate 2024-03-15 10:57:49
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {



    @Resource
    private PostMapper postMapper;


    @Resource
    private ReplyMapper replyMapper;

    @Override
    public void savePos(Post post) {
        postMapper.insert(post);
    }

    @Override
    public List<Post> findAllPost() {

        List<Post> postList = postMapper.selectList(null);

        for (Post post : postList) {//查询回复数

            QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pid", post.getPid());
            Long aLong = replyMapper.selectCount(queryWrapper);


            post.setReplyCount(aLong);
        }

        return postList;
    }

    @Override
    public Post findPostByPid(Long pid) {
        Post post = postMapper.selectById(pid);


        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid);
        Long aLong = replyMapper.selectCount(queryWrapper);


        post.setReplyCount(aLong);

        return post;
    }

    @Override
    public void deletePost(Long pid) {

        replyMapper.deleteById(pid);

        postMapper.deleteById(pid);

    }


}




