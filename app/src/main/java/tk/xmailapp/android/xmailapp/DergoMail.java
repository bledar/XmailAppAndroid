package tk.xmailapp.android.xmailapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class DergoMail extends Activity {

    EditText fusha_marresit;
    EditText fusha_dergusit ;
    EditText fusha_subjektit;
    EditText fusha_mesazhit;
    //ProgressBar progBar;
    Button btn_dego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dergo_mail);
        fusha_marresit   = (EditText)findViewById(R.id.fusha_marresi);
        fusha_dergusit   = (EditText) findViewById(R.id.fusha_derguesi);
        fusha_subjektit  = (EditText) findViewById(R.id.fusha_subjektit);
        fusha_mesazhit   = (EditText) findViewById(R.id.fusha_mesazhit);
        //progBar          = (ProgressBar) findViewById(R.id.progBar);
        btn_dego         = (Button) findViewById(R.id.btn_dergo);
        //progBar.setVisibility(View.GONE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dergo_mail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.btn_dergo:
                dergoMail();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }
    public void dergoMail(){
        String marresi=fusha_marresit.getText().toString();
        String derguesi=fusha_dergusit.getText().toString();
        String subjekti=fusha_subjektit.getText().toString();
        String mesazhi=fusha_mesazhit.getText().toString();

        if(fusha_dergusit.length()<2&&fusha_marresit.length()<2&&fusha_subjektit.length()<2&&fusha_mesazhit.length()<2){

            Toast.makeText(getApplicationContext(), "Ju lutem plotesoni formen!", Toast.LENGTH_LONG).show();

        }else{
            //progBar.setVisibility(View.VISIBLE);
            new DergoNeServer().execute(marresi, derguesi, subjekti, mesazhi);
            //Toast.makeText(getApplicationContext(), "Emaili po dergohet", Toast.LENGTH_SHORT).show();
        }


    }

    //----------------------------------------------------------------------------

    private class DergoNeServer extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0],params[1],params[2],params[3]);
            return null;
        }

        public void postData(String marresi,String derguesi,String subjekti,String mesazhi) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://bledarhaxhia.com/xmailapp.tk/proqesmail.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("marresi", marresi));
                nameValuePairs.add(new BasicNameValuePair("derguesi", derguesi));
                nameValuePairs.add(new BasicNameValuePair("subjekti", subjekti));
                nameValuePairs.add(new BasicNameValuePair("teksti", mesazhi));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

        protected void onPostExecute(Double result){
            Toast.makeText(getApplicationContext(), "Emaili u DERGUA", Toast.LENGTH_LONG).show();
            //progBar.setVisibility(View.GONE);
        }

        protected void onProgressUpdate(Integer... progress){
           // progBar.setProgress(progress[0]);
        }
    }
}


