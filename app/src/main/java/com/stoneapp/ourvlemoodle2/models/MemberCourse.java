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

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class MemberCourse{


    @SerializedName("id")
    int courseid;

    @SerializedName("fullname")
    String fullname;

    @SerializedName("shortname")
    String shortname;

    int memberid;

    public int getCourseId() {
            return courseid;
        }

    public String getFullname() {
            return fullname;
        }

    public String getShortname() {
            return shortname;
        }

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int userid) {
        this.memberid = memberid;
    }
}
