package com.progwor.prodelp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Prodelp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.progwor.prodelp.R.id.pdactivity_end_time_button;

public class FetchTask extends AsyncTask<String, Void, String[]> {

    Context context;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    Document doc;
    Elements links;
    int below;

    public FetchTask(Context context, RelativeLayout relativeLayout,int below) {
        super();
        this.context = context;
        this.relativeLayout = relativeLayout;
        this.below = below;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // params.addRule(RelativeLayout.BELOW,below);
        params.addRule(RelativeLayout.BELOW,relativeLayout.getChildAt(relativeLayout.getChildCount()-1).getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params.setMargins(16,16,16,16);
        relativeLayout.addView(progressBar,params);
    }

    @Override
    protected String[] doInBackground(String... params) {

        String query = params[0];
        query = query.replace(" ", "+");
        String title;


        try {
            doc = Jsoup.connect("http://www.google.com/search?q="+query)
                    .userAgent("Mozilla").timeout(5000).get();

            if(doc!=null){
                links = doc.select("a");
                String[] retLinks = new String[6];
                String link, text;
                int linkIndex = 34;

                for (int i = 0; i < 6; i++) {
                    link = links.get(linkIndex).attr("href");
                    if(link.contains("&sa")){
                        int endIndex = link.indexOf("&sa");
                        link = link.substring(7, endIndex);
                    }

                    text = links.get(linkIndex).text();

                    Log.d("link",link);

                   // if(link==null){
                    //    linkIndex++;
                   //     continue;
                   // }

                    if(i>=2){
                        if(link.contains(retLinks[i - 2])){
                            i--;
                            linkIndex++;
                            continue;
                        }
                    }
                    if (link.contains("cache:")
                            || link.contains("related:")
                            || link.contains("google")
                            || link.contains(query)
                            || link.contains("aclk")
                            || !link.contains("http")
                            || text.equals("")
                            || text.contains("http://")
                            || text.contains("Why this ad?")
                            ) {
                        i--;
                        linkIndex++;
                        continue;
                    }

                    /*if (i < 2) {
                        if (link.contains("cache:")
                                || link.contains("related:")
                                || link.contains("google")
                                || link.contains(query)
                                || link.contains("aclk")
                                || text.equals("")
                                || text.contains("http://")
                                ) {
                            linkIndex++;
                            continue;
                        }
                    } else{

                        Log.d("test", i + "");
                        if (link.contains("cache:")
                                || link.contains("related:")
                                || link.contains("google")
                                || link.contains(query)
                                || link.contains("aclk")
                                || text.equals("")
                                || text.contains("http://")
                                || link.contains(retLinks[i - 2])
                                ) {
                            i--;
                            linkIndex++;
                            continue;
                        }
                    }*/

                    if(link!=null)retLinks[i] = link;
                    if (i < 5) retLinks[++i] = text;
                    else break;
                }

                return retLinks;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final String[] s) {
        super.onPostExecute(s);
        relativeLayout.removeView(progressBar);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.autorecommend_view,null);

        TextView textView1 = (TextView) layout.findViewById(R.id.autorecommend_textview1);
        TextView textView2 = (TextView) layout.findViewById(R.id.autorecommend_textview2);
        TextView textView3 = (TextView) layout.findViewById(R.id.autorecommend_textview3);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);

try {
    textView1.setText(s[1]);
    textView1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri webpage = Uri.parse(s[0]);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(webIntent);
        }
    });
    textView2.setText(s[3]);
    textView2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri webpage = Uri.parse(s[2]);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(webIntent);
        }
    });

    textView3.setText(s[5]);
    textView3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri webpage = Uri.parse(s[4]);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(webIntent);
        }
    });
}
catch (NullPointerException e){
    return;
}
        for (int i = 1; i < 6; i = i + 2) {
            //Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            //Uri webpage = Uri.parse(s);
            //Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            //context.startActivity(webIntent);
            //if (s != null) adapter.add(s[i]);

        }




      /*  lv.setAdapter(adapter);
          lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if(position!=1)position=position+2;
                //Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                Uri webpage = Uri.parse(s[position * 2]);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                context.startActivity(webIntent);
            }
        });*/


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // params.addRule(RelativeLayout.BELOW,below);
        //params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        params.addRule(RelativeLayout.BELOW,below);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        //params.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);

       if(relativeLayout.findViewById(R.id.autorecommend)!=null){

           relativeLayout.removeView(relativeLayout.findViewById(R.id.autorecommend));
       }
       relativeLayout.addView(layout, params);
    }

}
