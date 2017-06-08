package com.svv.dms.app;

public class ACE {
	public static boolean ONLINE = false;
	public static String ADMIN_PHONE = "18611796139"; //rita
	public static String ADMIN_BIZ_PHONE = "13913124436"; //Martin
	
	final public static int HC_MAX_ACTIVE_NUM = 500; //顾问候选人最大Active数目
	final public static String TXT_LIMIT_ACTIVE_NUM = "顾问Active的候选人数量已达到上限 <span class=\"red\">"+HC_MAX_ACTIVE_NUM+" </span>人。";
	final public static String TXT_CANDIDATE_RECHECK = "亲~您来晚啦，该候选人已被其他猎头顾问提交";

	final public static String TXT_SMS_INVITE = "『[HR公司]』给您的候选人『[C]』发了面试邀请，请及时反馈（24小时后系统将自动拒绝）";
	final public static String TXT_SMS_HC_CHECKED = "%s，您申请注册的ACE顾问账号已经通过审核，请重新登录后体验更多功能。";
	final public static String TXT_SMS_HR_CHECKED = "%s，您获得ACE专属邀请码『%s』，请前往www.ace-elite.com注册体验。";
	final public static String TXT_EMAIL_SLOGAN = "100%活跃猎头库-专注于零售及奢侈品行业";
	final public static String TXT_SMS_ADMIN_HR = "客户『%s | %s』%s！";
	final public static String TXT_SMS_ADMIN_HC = "猎头『%s | %s』%s！";
	final public static String TXT_EMAIL_NOTCOM = "请使用公司邮箱注册";
	
	final public static String EMAILADDRESS = "service@ace-elite.com"; //发送邮箱
	final public static String EMAILPASSWORD = "Password01"; //密码
	
	final public static int USER_STATUS_NEW = 130001; //用户状态: 有效
	final public static int USER_CHECK_STATUS_NEW = 203000; //用户认证状态: 新增
	final public static int USER_CHECK_STATUS_CONFIRMING = 203001; //用户认证状态: 待审核
	final public static int USER_CHECK_STATUS_CHECKING = 203007; //用户认证状态：待认证
	
	final public static int USER_STATUS_VALID = 130001; //HR用户状态: 有效
	final public static int USER_CHECK_STATUS_NOPASS = 203044; //用户认证状态: 不通过
	final public static int USER_CHECK_STATUS_CHECKED = 203008; //用户认证状态: 已认证
	
	final public static int MM_STATUS_NEW = 130001; //新公司状态
	
    final public static String MM_BizStatus_NEW = "203000"; //公司认证状态: 待审核
    final public static String MM_BizStatus_ADDING = "203003"; //公司认证状态: 待新增
    final public static String MM_BizStatus_CHECKING = "203007"; //公司认证状态: 待认证（待签合同）
    final public static String MM_BizStatus_CHECKED = "203008"; //公司认证状态: 已认证
    final public static String MM_BizStatus_BLACKLIST = "203044"; //公司认证状态: 黑名单   

	final public static int SMS_TYPE_LOGIN = 138003;
	final public static int SMS_TYPE_HRPROCESS = 138022;
	final public static int SMS_TYPE_HCPROCESS = 138033;
	final public static int SMS_TYPE_ADMIN = 138099;
	
	final public static int CVFILE_TYPE_FACE = 250001; //附件类型：头像
	final public static int CVFILE_TYPE_CV = 250002; //附件类型：简历
	final public static int CVFILE_TYPE_OTHERS = 250009; //附件类型：其他

    

    final public static String ACTION_CODE_001 = "A000001"; //欢迎顾问
    final public static String ACTION_CODE_002 = "A000002"; //引导顾问新增候选人

	final public static String PROCESS_CODE_ST_APPLY = "220001"; //申请入库：需填申请入库时间
	final public static String PROCESS_CODE_ACTIVE_APPLY = "112013"; //申请Active
	final public static String PROCESS_CODE_ACTIVE = "112003"; //active候选人：需填时间
	final public static String PROCESS_CODE_INACTIVE = "112003"; //Inactive候选人：需填时间
	final public static String PROCESS_CODE_STCHECK_SUCCESS = "220002"; //入库审核通过
	final public static String PROCESS_CODE_ST_SUCCESS = "220006"; //入库：需填入库时间
	final public static String PROCESS_CODE_HCCOMMENT_SUCCESS = "220026"; //顾问面评通过：需填面评上线时间
	final public static String PROCESS_CODE_HCIV = "220003"; //顾问面试：需填面试时间
	final public static String PROCESS_CODE_HCIV_EDIT = "220023"; //顾问面试变更：需填面试时间
	final public static String PROCESS_CODE_HCCOMMENT_COMMIT = "220005"; //提交顾问面评：需填提交面评时间
	final public static String PROCESS_CODE_COLLECT = "331001"; //关注：需填时间
	final public static String PROCESS_CODE_UNCOLLECT = "331002"; //取消关注：需填时间
	final public static String PROCESS_CODE_ADDBLACKLIST = "331004"; //加入黑名单：需填时间
	final public static String PROCESS_CODE_DELBLACKLIST = "331024"; //移出黑名单：需填时间
	final public static String PROCESS_CODE_INVITE = "330000"; //邀请面试（流程开始）
	final public static String PROCESS_CODE_INVITE_SUCCESS = "330001"; //接受邀请(同意面试)
	final public static String PROCESS_CODE_ENTRY_CONFIRM = "330017"; //HC确定入职
	final public static String PROCESS_CODE_BILL = "550001"; //开票
	final public static String PROCESS_CODE_STOP = "330444"; //流程终止

    final public static String PROCESS_TYPE_HR_STANDARD = "252001"; //HR标准流程
    final public static String PROCESS_TYPE_HR_BILL = "252002"; //HR开票流程
    final public static String PROCESS_TYPE_ST = "252003"; //入库流程
    final public static String PROCESS_TYPE_STATUS = "252004"; //状态变更流程
    final public static String PROCESS_TYPE_CV = "252005"; //简历变更流程
    final public static String PROCESS_TYPE_HR_COLLECT = "252006"; //HR关注流程
    
    final public static String CANDI_STStatus_NOTST = "240000"; //未入库
    final public static String CANDI_STStatus_PREST = "240020"; //入库中
    final public static String CANDI_STStatus_ST = "240090"; //已入库
    final public static String CANDI_STStatus_BLACKLIST = "240444"; //黑名单    

    final public static String CANDI_ActiveStatus_ACTIVE = "247001"; //已上线
    final public static String CANDI_ActiveStatus_BLOCK = "247002"; //已入职
    final public static String CANDI_ActiveStatus_INACTIVE = "247004"; //已入库不上线
    final public static String CANDI_ActiveStatus_CHECKING_ACTIVE = "247005"; //上线申请审核中
    final public static String CANDI_CVStatus_CHECKING = "248001"; //待审核
    final public static String CANDI_CVStatus_UNPASS = "248004"; //已入职
    final public static String CANDI_CVStatus_ONLINE = "248009"; //已上线

    final public static String CANDI_STCheckStatus_NOTST = "241000"; //未入库
    final public static String CANDI_STCheckStatus_ST_CHECKING = "241001"; //入库申请审核中
    final public static String CANDI_STCheckStatus_STREFUSED = "241004"; //入库申请未通过
    final public static String CANDI_STCheckStatus_NO_IV = "241021"; //待安排顾问面试
    final public static String CANDI_STCheckStatus_IVING = "241022"; //已安排顾问面试
    final public static String CANDI_STCheckStatus_IVOVER = "241023"; //顾问面试结束
    final public static String CANDI_STCheckStatus_NO_HCCOMMENT = "241024"; //待提交顾问面评
    final public static String CANDI_STCheckStatus_HCCOMMENT_CHECKING = "241025"; //顾问面评审核中
    final public static String CANDI_STCheckStatus_HCCOMMENT_REFUSED = "241044"; //顾问面评未通过
    final public static String CANDI_STCheckStatus_HCCOMMENT_ONLINE = "241090"; //顾问面评已上线

    final public static String CANDI_STATUS_ING = "243001"; //进行中
    final public static String CANDI_STATUS_OVER = "243009"; //已结束

	final public static String COLLECT_STATUS_COLLECTED = "242001"; //已关注
	final public static String COLLECT_STATUS_CANCELED = "242002"; //已取消关注
	final public static String COLLECT_STATUS_BLACKLIST = "242004"; //黑名单

    final public static String CANDI_PROCESS_CLOSESTATUS_ENTRY = "245052"; //流程关闭状态：已入职
    final public static String CANDI_PROCESS_CLOSESTATUS_DIMISSION = "245054"; //流程关闭状态：已离职

    final public static String CANDI_BILL_STATUS_BILLING = "233000"; //票据状态：待开票
    final public static String CANDI_BILL_STATUS_BILLED = "233001"; //票据状态：已开票
    final public static String CANDI_BILL_STATUS_PayingToM = "233003"; //票据状态：待付款到平台
    final public static String CANDI_BILL_STATUS_PayingToM_OVERTIME = "233004"; //票据状态：付款到平台超时
    final public static String CANDI_BILL_STATUS_PayedToM = "233005"; //票据状态：已付款到平台
    final public static String CANDI_BILL_STATUS_PayingToHC = "233006"; //票据状态：待付款到顾问(HR提交后的状态)
    final public static String CANDI_BILL_STATUS_PayedToHC = "233007"; //票据状态：已付款到顾问
    final public static String CANDI_BILL_STATUS_PayingToHC_OVERTIME = "233008"; //票据状态：付款到顾问超时

    final public static String CANDI_PROCESS_INNERSTATUS_0 = "246000"; //流程内部状态：无
    final public static String CANDI_PROCESS_INNERSTATUS_INVITED = "246001"; //流程内部状态：已邀请面试
    final public static String CANDI_PROCESS_INNERSTATUS_WAIT_IV = "246005"; //流程内部状态：待安排面试
    final public static String CANDI_PROCESS_INNERSTATUS_IVING = "246006"; //流程内部状态：已安排面试
    final public static String CANDI_PROCESS_INNERSTATUS_IV_CONFIRM = "246007"; //流程内部状态：已确定面试
    final public static String CANDI_PROCESS_INNERSTATUS_IV_OVER = "246008"; //流程内部状态：面试结束
    final public static String CANDI_PROCESS_INNERSTATUS_IV_SUCCESS = "246009"; //流程内部状态：面试通过
    final public static String CANDI_PROCESS_INNERSTATUS_OFFER = "246020"; //流程内部状态：已Offer
    final public static String CANDI_PROCESS_INNERSTATUS_OFFER_UPLOADED = "246021"; //流程内部状态：已上传Offer
    final public static String CANDI_PROCESS_INNERSTATUS_INVITE_FAILED = "246042"; //流程内部状态：拒绝面试
    final public static String CANDI_PROCESS_INNERSTATUS_IV_FAILED = "246043"; //流程内部状态：面试不通过
    final public static String CANDI_PROCESS_INNERSTATUS_OFFER_FAILED = "246045"; //流程内部状态：未接受Offer
    final public static String CANDI_PROCESS_INNERSTATUS_NOTENTRY = "246046"; //流程内部状态：未入职
    final public static String CANDI_PROCESS_INNERSTATUS_OFFER_ACCEPTED = "246050"; //流程内部状态：已接受Offer
    final public static String CANDI_PROCESS_INNERSTATUS_ENTRY = "246052"; //流程内部状态：入职待确认
//    final public static String CANDI_PROCESS_INNERSTATUS_ENTRY_CONFIRM = "246053"; //流程内部状态：已确认入职 （跳过此状态，直接到【待开票】）
    final public static String CANDI_PROCESS_INNERSTATUS_DIMISSION_CHECKING = "246054"; //流程内部状态：离职待确认Dimission
    final public static String CANDI_PROCESS_INNERSTATUS_DIMISSION_CONFIRM = "246055"; //流程内部状态：确认离职Dimission
    final public static String CANDI_PROCESS_INNERSTATUS_BILLING = "246060"; //流程内部状态：待开票
    final public static String CANDI_PROCESS_INNERSTATUS_BILLED = "246061"; //流程内部状态：已开票
    final public static String CANDI_PROCESS_INNERSTATUS_PAYING_ToM = "246063"; //流程内部状态：待付款到平台
    final public static String CANDI_PROCESS_INNERSTATUS_PAYING_ToM_OVERTIME = "246064"; //流程内部状态：付款到平台超时
    final public static String CANDI_PROCESS_INNERSTATUS_PAYED_ToM = "246065"; //流程内部状态：已付款到平台
    final public static String CANDI_PROCESS_INNERSTATUS_PAYING_ToHC = "246066"; //流程内部状态：待付款到顾问
    final public static String CANDI_PROCESS_INNERSTATUS_PAYED_ToHC = "246067"; //流程内部状态：已付款到顾问
    final public static String CANDI_PROCESS_INNERSTATUS_PAYING_ToHC_OVERTIME = "246068"; //流程内部状态：付款到顾问超时
    final public static String CANDI_PROCESS_INNERSTATUS_COMPLETE = "246090"; //流程内部状态：已完成
    final public static String CANDI_PROCESS_INNERSTATUS_CLOSE = "246444"; //流程内部状态：关闭


    final public static String MM_RELATION_TYPE_IN = "239001"; //在职
    final public static String MM_RELATION_TYPE_YES = "239002"; //意向
    final public static String MM_RELATION_TYPE_NO = "239004"; //屏蔽
    
    final public static String ACTION_TYPE_TASK = "254001"; //任务
    final public static String ACTION_TYPE_NOTIFY = "254002"; //提醒
    final public static String ACTION_TYPE_NOTICE = "254003"; //通知
    final public static String ACTION_TYPE_ALARM = "254004"; //告警

    final public static String NOTIFY_TYPE_NO_NOTIFY = "255000"; //不提醒
    final public static String NOTIFY_TYPE_NOTICE = "255001"; //通知
    final public static String NOTIFY_TYPE_NOTICE_CONFIRM = "255002"; //待确认通知
    final public static String NOTIFY_TYPE_TASK = "255003"; //任务提醒
    final public static String NOTIFY_TYPE_TASK_LIMITTIME = "255004"; //限时任务提醒
    
}
