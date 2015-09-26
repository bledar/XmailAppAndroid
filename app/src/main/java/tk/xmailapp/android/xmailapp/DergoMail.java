package tk.xmailapp.android.xmailapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/*

Klasa DergoMail eshte baza e aplikacionit tone pasi ketu behen te gjitha inicializimet fillestare
dhe merren te gjitha te dhenat e nevojshme dhe pregatiten per dergimin e emailt
Gjithashtu ketu behet edhe menagjimi i te gjitha gabimeve te mundshme
Ajo trashegohet nga klasa baze e Androidit qe menagjon Aktivitet

*/

public class DergoMail extends Activity {

    EditText fusha_marresit;
    EditText fusha_dergusit ;
    EditText fusha_subjektit;
    EditText fusha_mesazhit;
    Button btn_dego;
    ProgressDialog progress;
    String njoftimi  = "Bosh";//Variabli qe ruan njoftimin qe do shfaqet per perdoruesin.
    boolean sukses=false; //Behet true ne momentin qe te dhenat jane derguar me sukses ne server.
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dergo_mail);
        fusha_marresit   = (EditText)findViewById(R.id.fusha_marresi);
        fusha_dergusit   = (EditText) findViewById(R.id.fusha_derguesi);
        fusha_subjektit  = (EditText) findViewById(R.id.fusha_subjektit);
        fusha_mesazhit   = (EditText) findViewById(R.id.fusha_mesazhit);
        btn_dego         = (Button) findViewById(R.id.btn_dergo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dergo_mail, menu);
        /*fusha_marresit.setText("bledi-1@live.com");
        fusha_dergusit.setText("bledihaxhia@gmail.com");
        fusha_subjektit.setText("dergoje");
        fusha_mesazhit.setText("Nje mesazh i derguar nga android.");*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btn_dergo:
                dergoMail();
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
            njoftimi="Ju lutem plotesoni formen!";
            Toast.makeText(getApplicationContext(),njoftimi , Toast.LENGTH_LONG).show();

        }else{
            progress = ProgressDialog.show(this, "XmailApp","Emaili po dergohet ne server!", true);
            new DergoNeServer().execute(marresi, derguesi, subjekti, mesazhi);
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
            HttpPost httppost = new HttpPost("http://xmailapp.tk/proqesmail.php");

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
                int responseCode = response.getStatusLine().getStatusCode();
                switch(responseCode) {
                    case 200:
                        HttpEntity entity = response.getEntity();
                        if(entity != null) {
                            String responseBody = EntityUtils.toString(entity);
                            Log.v("Gabim", "Pergjigjia e serverit: "+responseBody);
                            njoftimi= ""+responseBody;
                            sukses=true;
                        }
                        break;
                }

                Log.v("Gabim", "Kodi i serverit: "+responseCode);

            } catch (ClientProtocolException e) {
                Log.v("Gabim", "Gabin ne protokoll");
                sukses=false;

            } catch (IOException e) {
                Log.v("Gabim", "Gabin ne komunikim");
                sukses=false;
            }
        }

        protected void onPostExecute(Double result){
            fusha_marresit.setText("");
            fusha_dergusit.setText("");
            fusha_subjektit.setText("");
            fusha_mesazhit.setText("");
            progress.dismiss();
            if(sukses){
                Toast.makeText(getApplicationContext(), njoftimi, Toast.LENGTH_LONG).show();
            }else {
                Njoftimi();
            }
        }
    }

    public void Njoftimi(){
        AlertDialog.Builder Alerti = new AlertDialog.Builder(DergoMail.this);
        Alerti.setCancelable(true);
        Alerti.setMessage("Problem ne dergimin e te dhenave ne server.\n Kontrollo lidhjen e internetit!");
        Alerti.setTitle("GABIM KOMUNIKIMI");
        AlertDialog dialog = Alerti.create();
        dialog.show();
    }

}


