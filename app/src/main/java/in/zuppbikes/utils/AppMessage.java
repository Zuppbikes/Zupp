package in.zuppbikes.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import in.zuppbikes.R;
import in.zuppbikes.ZuppApplication;


public class AppMessage {

    public void showAlertYesNo(String iMessage, Context iContext, DialogInterface.OnClickListener iYesListener, DialogInterface.OnClickListener iNoListener) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
//        ((TextView)dialog.findViewById(android.R.id.message)).setTypeface(TypefaceUtil.getFont());
//        dialog.setTitle(iContext.getString(R.string.alert_title_info));
//        dialog.setMessage(iMessage);
        View aView = getView(iContext);
        dialog.setView(aView);
        TextView aTitle = aView.findViewById(R.id.tvTitle);
        TextView aMessage = aView.findViewById(R.id.tvMessage);
        aTitle.setText(iContext.getString(R.string.alert_title_info));
        aMessage.setText(iMessage);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(R.string.yes), iYesListener);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, iContext.getString(R.string.no), iNoListener);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void showAlertOkCallback(String iMessage, Context iContext, DialogInterface.OnClickListener iYesListener) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
        View aView = getView(iContext);
        dialog.setView(aView);
        TextView aTitle = aView.findViewById(R.id.tvTitle);
        TextView aMessage = aView.findViewById(R.id.tvMessage);
        aTitle.setText(iContext.getString(R.string.alert_title_info));
        aMessage.setText(iMessage);
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(android.R.string.ok), iYesListener);
        dialog.show();
    }

    public void showAlertRetryCallback(String iMessage, Context iContext, DialogInterface.OnClickListener iYesListener) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
        View aView = getView(iContext);
        dialog.setView(aView);
        TextView aTitle = aView.findViewById(R.id.tvTitle);
        TextView aMessage = aView.findViewById(R.id.tvMessage);
        aTitle.setText(iContext.getString(R.string.alert_title_info));
        aMessage.setText(iMessage);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(R.string.retry), iYesListener);
        dialog.show();
    }

    public void showAlertRetryWithCancelCallback(String iMessage, Context iContext, DialogInterface.OnClickListener iYesListener, DialogInterface.OnClickListener iNoListener) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
        View aView = getView(iContext);
        dialog.setView(aView);
        TextView aTitle = aView.findViewById(R.id.tvTitle);
        TextView aMessage = aView.findViewById(R.id.tvMessage);
        aTitle.setText(iContext.getString(R.string.alert_title_info));
        aMessage.setText(iMessage);
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(R.string.retry), iYesListener);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, iContext.getString(R.string.cancel), iNoListener);
        dialog.show();
    }


    public void showAlertOkCallback(String iTitle, String iMessage, Context iContext, DialogInterface.OnClickListener iYesListener) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
        View aView = getView(iContext);
        dialog.setView(aView);
        TextView aTitle = aView.findViewById(R.id.tvTitle);
        TextView aMessage = aView.findViewById(R.id.tvMessage);
        aTitle.setText(iTitle);
        aMessage.setText(iMessage);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(android.R.string.ok), iYesListener);
        dialog.show();
    }

    /**
     * Shows toast with dynamic message
     *
     * @param iMessage
     */
    public static void showToast(String iMessage) {
        LayoutInflater aInflater = (LayoutInflater) ZuppApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View aCustomView = aInflater.inflate(R.layout.layout_toast, null);
        TextView aMessageView = aCustomView.findViewById(R.id.message);
        aMessageView.setText(iMessage);

        Toast aToast = new Toast(ZuppApplication.getInstance());
        aToast.setGravity(Gravity.BOTTOM, 0, 100);
        aToast.setDuration(Toast.LENGTH_SHORT);
        aToast.setView(aCustomView);
        aToast.show();
    }

    /**
     * Shows toast with dynamic message
     *
     * @param iMessage
     */
    public void showCenterToast(String iMessage) {
        LayoutInflater aInflater = (LayoutInflater) ZuppApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View aCustomView = aInflater.inflate(R.layout.layout_toast, null);
        TextView aMessageView = aCustomView.findViewById(R.id.message);
        aMessageView.setText(iMessage);

        Toast aToast = new Toast(ZuppApplication.getInstance());
        aToast.setGravity(Gravity.CENTER, 0, 0);
        aToast.setDuration(Toast.LENGTH_SHORT);
        aToast.setView(aCustomView);
        aToast.show();
    }

    private View getView(Context iContext) {
        return LayoutInflater.from(iContext).inflate(R.layout.layout_custom_dialog, null);
    }

}
