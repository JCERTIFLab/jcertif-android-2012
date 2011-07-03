package com.jcertif.android.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.jcertif.android.model.Event;
import com.jcertif.android.model.Speaker;

/**
 * Session detail View.
 *
 * @author: rossi.oddet
 */
public class SessionDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail);

        Event ev = findEvent();
        Speaker sp = findSpeaker();

        TextView headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("Id session" + getIntent().getIntExtra("sessionId", -1));

        TextView eventDetail = (TextView) findViewById(R.id.eventDetail);
        eventDetail.setText(ev.description);

        TextView spName = (TextView) findViewById(R.id.speakerName);
        spName.setText(sp.firstName + " " + sp.lastName);

        TextView spBio1 = (TextView) findViewById(R.id.speakerBioPart1);
        spBio1.setText(sp.bio);

        TextView spBio2 = (TextView) findViewById(R.id.speakerBioPart2);
        spBio2.setText("session id = " + getIntent().getIntExtra("sessionId", -1));


    }

    private Event findEvent() {
        int sessionId = getIntent().getIntExtra("sessionId", -1);

        Event ev = new Event();

        ev.name = "Event name";
        ev.description = "Event description";


        return ev;
    }

    private Speaker findSpeaker() {
        Speaker sp = new Speaker();
        sp.lastName = "Oddet";
        sp.firstName = "Rossi";
        sp.bio = "ifnmionfio cdvnionvpoe cz,poferzjù ,cpz   kfkerz^,pock,z^pkc^p*z";
        return sp;
    }
}
