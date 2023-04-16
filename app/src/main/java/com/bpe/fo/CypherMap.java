package com.bpe.fo;

import java.sql.Time;

public class CypherMap {
    String Name;
    String Time_Stamp;
    String Cypher_Text;

    public void NameTimeCypherSeparator(String data) {
        String[] parts = data.split("-");
        String countryName = parts[0];
        String[] timeStampAndCypherText = parts[1].split(" ", 2);
        String timeStamp = timeStampAndCypherText[0];
        String cypherText = timeStampAndCypherText[1];
        this.setName(countryName);
        this.setTime_Stamp(timeStamp);
        this.setCypher_Text(cypherText);
    }
    public void setName(String name) {
        Name = name;
    }

    public void setTime_Stamp(String time_Stamp) {
        Time_Stamp = time_Stamp;
    }

    public void setCypher_Text(String cypher_Text) {
        Cypher_Text = cypher_Text;
    }

    public String getName() {
        return Name;
    }

    public String getTime_Stamp() {
        return Time_Stamp;
    }

    public String getCypher_Text() {
        return Cypher_Text;
    }
}
