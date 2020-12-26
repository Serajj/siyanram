package com.verbosetech.weshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.verbosetech.weshare.R;
import com.verbosetech.weshare.activity.AddDetailActivity;
import com.verbosetech.weshare.activity.MainActivity;
import com.verbosetech.weshare.model.Detail;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> listDataHeader;
    HashMap<String, List<String>> listHashMap;
    Detail data;
    String _token;
    UserResponse profileMe;
    String type;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap, Detail data, String _token,String type) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.data = data;
        this._token = _token;
        this.profileMe = Helper.getLoggedInUser(new SharedPreferenceUtil(context));
        this.type = type;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
       String headerTitle=(String)getGroup(groupPosition);
       View view= LayoutInflater.from(context).inflate(R.layout.list_group,parent,false);
        TextView lv_list_header=view.findViewById(R.id.lv_list_group);

        lv_list_header.setTypeface(null, Typeface.BOLD);
        lv_list_header.setText(headerTitle);


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        /**String childText=(String)getChild(groupPosition,childPosition);
        View view= LayoutInflater.from(context).inflate(R.layout.listitem,parent,false);
        TextView textChild=view.findViewById(R.id.lv_list_item);
        textChild.setText(childText);**/
        View view;
        if (groupPosition==0){
            view= LayoutInflater.from(context).inflate(R.layout.list_aboutme,parent,false);
            EditText gender = view.findViewById(R.id.gender);
            EditText dob = view.findViewById(R.id.dob);
            ImageButton editBtn=view.findViewById(R.id.edit_btn);
            EditText fname = view.findViewById(R.id.fname);
            EditText mName = view.findViewById(R.id.mName);
            EditText occupation = view.findViewById(R.id.occupation);

            if (type.equalsIgnoreCase("edit")){
                editBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setVisibility(View.GONE);
            }

            if (profileMe!=null){
                gender.setText(profileMe.getGender());
                dob.setText(profileMe.getDob());
                fname.setText(profileMe.getFname());
                mName.setText(profileMe.getMname());
                occupation.setText(profileMe.getOccupation());
            }

            
        }else if (groupPosition==1){
             view= LayoutInflater.from(context).inflate(R.layout.family_detail,parent,false);
            EditText ftype = view.findViewById(R.id.ftype);
            ImageButton editBtn=view.findViewById(R.id.edit_btn);
            EditText fvalue = view.findViewById(R.id.fvalue);
            EditText fstatus = view.findViewById(R.id.fstatus);
            EditText fincome = view.findViewById(R.id.fincome);
            EditText foccupation = view.findViewById(R.id.foccupation);
            EditText fbrothers = view.findViewById(R.id.brother);
            EditText fsisters = view.findViewById(R.id.sister);
            if (data!=null){
                ftype.setText(data.getFtype());
                fvalue.setText(data.getFvalue());
                fstatus.setText(data.getFstatus());
                fincome.setText(data.getFincome());
                foccupation.setText(data.getFoccupation());
                fbrothers.setText(data.getFbrother());
                fsisters.setText(data.getFsister());
            }
            if (type.equalsIgnoreCase("edit")){
                editBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setVisibility(View.GONE);
            }
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AddDetailActivity.class);
                    intent.putExtra("token",_token);
                    intent.putExtra("layout",groupPosition);
                    context.startActivity(intent);
                }
            });
        }else if (groupPosition==2){
             view= LayoutInflater.from(context).inflate(R.layout.list_education,parent,false);

            EditText etHeducation = view.findViewById(R.id.heducation);
            EditText etUgdegree = view.findViewById(R.id.ugdegree);
            EditText etUgcollege = view.findViewById(R.id.ugcollege);
            EditText etPgdegree = view.findViewById(R.id.pgdegree);
            ImageButton editBtn=view.findViewById(R.id.edit_btn);
            EditText etPgcollege = view.findViewById(R.id.pgcollege);
            if (data!=null){
                etHeducation.setText(data.getEhighest());
                etUgdegree.setText(data.getEugdegree());
                etUgcollege.setText(data.getEugcollege());
                etPgdegree.setText(data.getEpgdegree());
                etPgcollege.setText(data.getEpgcollege());
            }
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AddDetailActivity.class);
                    intent.putExtra("token",_token);
                    intent.putExtra("layout",groupPosition);
                    context.startActivity(intent);
                }
            });
            if (type.equalsIgnoreCase("edit")){
                editBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setVisibility(View.GONE);
            }
        }else if (groupPosition==3){
            view= LayoutInflater.from(context).inflate(R.layout.list_desire,parent,false);
            EditText etdHeShouldBe = view.findViewById(R.id.heshouldbe);
            EditText etdEducation = view.findViewById(R.id.deducation);
            EditText etdAgeofAround = view.findViewById(R.id.ageofaround);
            EditText etdMartialStatus = view.findViewById(R.id.martialstatus);
            EditText etdChallenged = view.findViewById(R.id.challenged);
            EditText etdReligion = view.findViewById(R.id.religion);
            EditText etdMotherTounge = view.findViewById(R.id.mothertounge);
            EditText etdCaste = view.findViewById(R.id.caste);
            EditText etdCity = view.findViewById(R.id.dcity);
            EditText etdCountry = view.findViewById(R.id.dcountry);
            ImageButton editBtn=view.findViewById(R.id.edit_btn);
            if (data!=null){
                etdHeShouldBe.setText(data.getDheshouldbe());
                etdAgeofAround.setText(data.getDageofaround());
                etdMartialStatus.setText(data.getDmartialstatus());
                etdChallenged.setText(data.getDchallenged());
                etdReligion.setText(data.getDreligion());
                etdMotherTounge.setText(data.getDmothertounge());
                etdCaste.setText(data.getDcaste());
                etdCity.setText(data.getDcity());
                etdCountry.setText(data.getDcountry());
                etdEducation.setText(data.getDeducation());
            }
            if (type.equalsIgnoreCase("edit")){
                editBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setVisibility(View.GONE);
            }
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AddDetailActivity.class);
                    intent.putExtra("token",_token);
                    intent.putExtra("layout",groupPosition);
                    context.startActivity(intent);
                }
            });
        }
        else{
             view= LayoutInflater.from(context).inflate(R.layout.list_career,parent,false);
            EditText etcHighesQualification = view.findViewById(R.id.highestqualification);
            EditText etcEmployedIn = view.findViewById(R.id.employedin);
            EditText etcAnnualIncome = view.findViewById(R.id.annualincome);
            ImageButton editBtn=view.findViewById(R.id.edit_btn);
            if (data!=null){
                etcHighesQualification.setText(data.getChighesteducation());
                etcEmployedIn.setText(data.getCemployedin());
                etcAnnualIncome.setText(data.getCannuanincome());
            }

            if (type.equalsIgnoreCase("edit")){
                editBtn.setVisibility(View.VISIBLE);
            }else{
                editBtn.setVisibility(View.GONE);
            }
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AddDetailActivity.class);
                    intent.putExtra("token",_token);
                    intent.putExtra("layout",groupPosition);
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
