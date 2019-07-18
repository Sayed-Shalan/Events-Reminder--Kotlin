package com.sayed.thirdway.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder
import androidx.appcompat.app.AlertDialog
import com.sayed.thirdway.other.OkCancelCallback
import com.sayed.thirdway.ui.base.BaseActivity


public class AppUtils {

    //Static data
    companion object{
        //method for check if the device is connected to internet or no
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        @JvmStatic
        fun navigateUp(activity: BaseActivity) {
            val upIntent = NavUtils.getParentActivityIntent(activity)
            if (upIntent == null) {
                activity.finish()
                //            if (activity.getResources().getBoolean(R.bool.is_right_to_left)) activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                //            else activity.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            } else if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(activity)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities()
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(activity, upIntent)
            }
        }


        //open ok cancel Dialog
        fun buildOkCancelDialog(activity: Activity, title: String, okStr: String, cancelStr: String, okCancelCallback: OkCancelCallback) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(okStr) { dialog, which -> okCancelCallback.onOkClick() }
                .setNegativeButton(cancelStr) { dialog, which ->
                    okCancelCallback.onCancelClick()
                    dialog.cancel()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

}