package com.hallo.mall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName mall_pay_info
 */
@TableName(value ="mall_pay_info")
@Data
@NoArgsConstructor
public class PayInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 支付平台:1-支付宝,2-微信
     */
    private Integer payPlatform;

    /**
     * 支付流水号
     */
    private String platformNumber;

    /**
     * 支付状态
     */
    private String platformStatus;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;



    public PayInfo(Long orderNo, Integer payPlatform, String platformStatus, BigDecimal payAmount) {
        this.orderNo = orderNo;
        this.payPlatform = payPlatform;
        this.platformStatus = platformStatus;
        this.payAmount = payAmount;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PayInfo other = (PayInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getPayPlatform() == null ? other.getPayPlatform() == null : this.getPayPlatform().equals(other.getPayPlatform()))
            && (this.getPlatformNumber() == null ? other.getPlatformNumber() == null : this.getPlatformNumber().equals(other.getPlatformNumber()))
            && (this.getPlatformStatus() == null ? other.getPlatformStatus() == null : this.getPlatformStatus().equals(other.getPlatformStatus()))
            && (this.getPayAmount() == null ? other.getPayAmount() == null : this.getPayAmount().equals(other.getPayAmount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getPayPlatform() == null) ? 0 : getPayPlatform().hashCode());
        result = prime * result + ((getPlatformNumber() == null) ? 0 : getPlatformNumber().hashCode());
        result = prime * result + ((getPlatformStatus() == null) ? 0 : getPlatformStatus().hashCode());
        result = prime * result + ((getPayAmount() == null) ? 0 : getPayAmount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", payPlatform=").append(payPlatform);
        sb.append(", platformNumber=").append(platformNumber);
        sb.append(", platformStatus=").append(platformStatus);
        sb.append(", payAmount=").append(payAmount);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}