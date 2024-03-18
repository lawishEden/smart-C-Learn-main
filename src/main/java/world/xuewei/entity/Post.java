package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName post
 */
@TableName(value ="post")
@Data
public class Post implements Serializable {
    /**
     * 
     */
    @TableId(value = "pid")
    private Integer pid;

    /**
     * 
     */
    @TableField(value = "ptitle")
    private String ptitle;

    /**
     * 
     */
    @TableField(value = "pbody")
    private String pbody;

    /**
     * 
     */
    @TableField(value = "psendtime")
    private Date psendtime;

    /**
     * 
     */
    @TableField(value = "lastreplytime")
    private Date lastreplytime;

    /**
     * 
     */
    @TableField(value = "id")
    private Integer id;


    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Long replyCount;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}