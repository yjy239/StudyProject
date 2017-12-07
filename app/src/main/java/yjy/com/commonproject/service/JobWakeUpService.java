package yjy.com.commonproject.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

/**
 * Created by asus on 2017/7/23.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {

    private static final int JobId = 2;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启轮询
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JobId,new ComponentName(this,JobWakeUpService.class));
        jobBuilder.setPeriodic(2000);
        JobScheduler service =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        service.schedule(jobBuilder.build());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //开启定时任务，定时轮询，查看messageservice有没有被杀死,杀死则启动
        boolean messageServiceAlive = isServiceWorked(MessageService.class.getName());
        if(messageServiceAlive){
            startService(new Intent(JobWakeUpService.this,MessageService.class));
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    /**判断某个服务是否管理***/
    private boolean isServiceWorked(String serviceName){
        boolean isWork = false;
        ActivityManager myAM =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mList = myAM.getRunningServices(100);
        if(mList.size() <= 0){
            return false;
        }
        for(int i = 0;i<mList.size();i++){
            String mName = mList.get(i).service.getClassName().toString();
            if(mName.equals(serviceName)){
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
