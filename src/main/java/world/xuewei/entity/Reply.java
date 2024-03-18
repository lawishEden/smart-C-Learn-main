package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName reply
 */
@TableName(value ="reply")
@Data
public class Reply implements Serializable {
    /**
     * 
     */
    @TableId(value = "rid", type = IdType.AUTO)
    private Long rid;

    /**
     * 
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 
     */
    @TableField(value = "uid")
    private Integer uid;

    /**
     * 
     */
    @TableField(value = "replymessage")
    private String replymessage;

    /**
     * 
     */
    @TableField(value = "replytime")
    private Date replytime;


    @TableField(exist = false)
    private User user;


    @TableField(exist = false)
    private Post post;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}