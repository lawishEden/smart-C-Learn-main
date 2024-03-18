package world.xuewei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import world.xuewei.entity.Post;

import java.util.List;

/**
* @author 26722
* @description 针对表【post】的数据库操作Service
* @createDate 2024-03-15 10:57:49
*/
public interface PostService extends IService<Post> {

    /**
     * 保存帖子
     *
     * @param post
     */
    void savePos(Post post);

    /**
     * 查询所有帖子
     *
     * @return
     */
    List<Post> findAllPost();

    /**
     * 根据帖子id查询帖子
     *
     * @param pid
     * @return
     */
    Post findPostByPid(Long pid);


    /**
     * 根据帖子id删除帖子
     *
     * @param pid
     */
    void deletePost(Long pid);



}
