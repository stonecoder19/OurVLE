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

package com.stoneapp.ourvlemoodle2.sync;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.stoneapp.ourvlemoodle2.models.Member;
import com.stoneapp.ourvlemoodle2.rest.RestMembers;

import java.util.List;

public class MemberSync {

    private String token;
    private List<Member> members;

    public MemberSync(String token) {
        this.token = token;
    }

    public boolean syncMembers(String courseid) {

        RestMembers mrmembers = new RestMembers(token);

        members = mrmembers.getMembers(courseid); // gets a list of members from api call

        if (members == null) return false;

        if (members.size() == 0) return false;


        ActiveAndroid.beginTransaction();
        try {
            deleteStaleData();
            for (int i = 0; i < members.size(); i++) {
                final Member member = members.get(i);
                member.setCourseid(courseid);

                Member.findOrCreateFromJson(member); // saves contact to database
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }

    private void deleteStaleData()
    {

        List<Member> stale_members = new Select().all().from(Member.class).execute();
        for(int i=0;i<stale_members.size();i++)
        {
            if(!doesMemberExistInJson(stale_members.get(i)))
            {
                Member.delete(Member.class,stale_members.get(i).getId());
            }
        }
    }

    private boolean doesMemberExistInJson(Member member)
    {
        return members.contains(member);
    }

}
