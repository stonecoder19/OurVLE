/*
 * Copyright 2016 Matthew Stone and Romario Maxwell.
 *
 * This file is part of OurVLE.
 *
 * OurVLE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OurVLE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OurVLE.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.stoneapp.ourvlemoodle2.models;

import android.graphics.PorterDuff;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

@Table(name="MoodleEvent")
public class MoodleEvent extends Model{

    @Column(name="eventid")
    @SerializedName("id")
    int eventid;//event id

    @Column(name="name")
    @SerializedName("name")
    String name;//event name

    @Column(name="description")
    @SerializedName("description")
    String description;   //Optional //Description

    @SerializedName("format")
    int format;   //description format (1 = HTML, 0 = MOODLE, 2 = PLAIN or 4 = MARKDOWN)

    @Column(name="courseid")
    @SerializedName("courseid")
    int courseid;  //course id

    @SerializedName("groupid")
    int groupid;   //group id

    @SerializedName("userid")
    int userid;  //user id

    @SerializedName("repeatid")
    int repeatid;   //repeat id

    @SerializedName("modulename")
    String modulename;  //  Optional //module name

    @SerializedName("instance")
    int instanceid;   //instance id

    @SerializedName("eventtype")
    String eventtype;  //Event type

    @Column(name="timestart")
    @SerializedName("timestart")
    int timestart;  //timestart

    @Column(name="timeduration")
    @SerializedName("timeduration")
    int timeduration;   //time duration

    @SerializedName("visible")
    int visible;  //visible

    @SerializedName("uuid")
    String uuid;  //  Optional //unique id of ical events

    @SerializedName("sequence")
    int sequence;  //sequence

    @SerializedName("timemodified")
    int timemodified; //time modified

    @SerializedName("subscriptionid")
    int subscriptionid;

    String coursename;

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCoursename() {

        return coursename;
    }

    public int getEventid() {
        return eventid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getFormat() {
        return format;
    }

    public int getCourseid() {
        return courseid;
    }

    public int getGroupid() {
        return groupid;
    }

    public int getUserid() {
        return userid;
    }

    public int getRepeatid() {
        return repeatid;
    }

    public String getModulename() {
        return modulename;
    }

    public int getInstanceid() {
        return instanceid;
    }

    public String getEventtype() {
        return eventtype;
    }

    public int getTimestart() {
        return timestart;
    }

    public int getTimeduration() {
        return timeduration;
    }

    public int getVisible() {
        return visible;
    }

    public String getUuid() {
        return uuid;
    }

    public int getSequence() {
        return sequence;
    }

    public int getTimemodified() {
        return timemodified;
    }

    public int getSubscriptionid() {
        return subscriptionid;
    }

    public static MoodleEvent findOrCreateFromJson(MoodleEvent new_event) {
        int eventid = new_event.getEventid();
        MoodleEvent existingEvent =
                new Select().from(MoodleEvent.class).where("eventid = ?", eventid).executeSingle();
        if (existingEvent != null) {
            // found and return existing
            UpdateEvent(existingEvent,new_event);
            return existingEvent;
        } else {
            // create and return new user
            MoodleEvent event = new_event;
            event.save();
            return event;
        }
    }

    private static void UpdateEvent(MoodleEvent old_event,MoodleEvent new_event)
    {
        old_event = new_event;
        old_event.save();

    }


}
