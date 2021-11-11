package com.codeqm.mpay.base

/**
 * 描述：支付策略
 * @author CQM
 * @time   2021/11/11
 */
interface PayStrategy {

    /**
     * 支付操作
     */
    fun payAction()

}