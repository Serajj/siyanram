package com.verbosetech.weshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.verbosetech.weshare.R;
import com.verbosetech.weshare.model.Detail;
import com.verbosetech.weshare.model.Dropdown;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDetailActivity extends AppCompatActivity {

    EditText etAbout,etdEducation,etdHeShouldBe,
           etUgcollege,etcHighesQualification,etPgcollege, etFbrothers, etFsisters;
    RelativeLayout aboutRl,careerRl,desireRl,educationRl,familyRl;
    Spinner etAcaste,etAreligion,etAheight,etAmartialStatus,etAmotherTounge,etAProfileFor,etAcomplexion,etAbodyType,sManglik,sGotra,sMamagotra,sNaniGotra;
    Spinner etFtype, etFvalue,etFstatus, etFincome, etFoccupation;

    Spinner etHeducation, etUgdegree, etPgdegree, etcEmployedIn, etcAnnualIncome;

    Spinner etdAgeofAround, etdMartialStatus, etdChallenged, etdReligion, etdMotherTounge, etdCaste, etdCity, etdCountry;
    ArrayAdapter<String>   adapterdMotherTounge, adapterdCaste, adapterdCity,adapterdCountry,adapterdAgeofAround;
    ArrayAdapter<CharSequence>  adapterdMartialStatus,  adapterdChallenged,  adapterdReligion;


    ArrayAdapter<CharSequence> adapteretHeducation, adapteretUgdegree, adapterPgdegree, adapterEmployedIn;

    ArrayAdapter<CharSequence> adapterFtype, adapterFvalue,adapterFstatus, adapterFoccupation;
    ArrayAdapter<CharSequence> adapterReligion,adapterMartialStatus,adapterProfileFor,adapterComplexion,adapterAbodyType,adapterManglik,adapterSgotra,adapterNgotra,adapterMgotra;

    ArrayAdapter<String> adapterCaste,adapterMotherTounge,adapterHeight,adapterFincome, adaptercAnnualIncome;;

    String[] Religion = {"Hindu", "Muslim","Sikh","Christian","Buddhist","Jain","Parsi","Jewish","Bahai"};
    List<String> Casted = new ArrayList<>();
    List<String> MotherTounge = new ArrayList<>();
    List<String> Height = new ArrayList<>();
    List<String> City = new ArrayList<>();
    List<String> State = new ArrayList<>();
    List<String> AgeAround = new ArrayList<>();
    String[] Mstatus = {"Never Married", "Awaiting Divorce","Divorced","Widowed","Annulled","Married"};
    String [] OptionYN={"No","Yes","Doesn't Matter"};
    String[] ProfileFor={"My Self","Son","Daughter","Friend"};
    String[] Complexion={"Fair","Medium Fair","Dark Brown","Brown","Olive"};
    String[] Bodytype={"Slim","Athletic","Average","Heavy"};
    String[] Manglik={"Manglik","Non-Manglik","Don't Know"};
    String[] Gotra={
            "Don't Know",
            "Aadisya (Gotra) (Brahmin)",
            "Agni (Gotra)",

            "Angiras Brahmin (caste and gotra)",
            "Alambayan (Shakdwipi Brahmin and Vaidyabrahmin)",
            "Atri (Surname and gotra)",
            "Awasthi (surname and gotra)",
            "Bachchas (Rajputs) (Gaur Brahmin)",
            "Bansal (surname and gotra) bhesean gotra of pandit",
            "Bhargava (after sage Bhrigu)",
            "Bhardwaja (after sage Bhardwaja)",
            "Baghel (caste and gotra)",
            "Chaudhary (surname) (Jat surname)",
            "Dahiya (Jat surname)",
            "Deval (Surname and gotra)",
            "Dubey (surname and gotra)",
            "Gangotri (Brahmin surname)",
            "Gautam (surname and gotra)",
            "Gohel (Rajput and others; also Gahlot, Gehlot, Gohil, Gelot)",
            "Goyal (surname and gotra)",
            "Harita (Surname and gotra)",
            "Jadaun (Rajput and Gurdar)",
            "Kansal (surname and gotra)",
            "Kapistal (Gotra)",
            "Kashyapa (after sage Kashyapa)",
            "Kaundinya gotra (Jamwal pandit gotra and Kamdala gotra Kaundinya)",
            "Kaushal (surname and gotra)",
            "Kaushik (Surname and gotra of Barnwal",
            "Vishwamitra/Kaushik (Brahmin surname and gotra)",
            "Mittal (Agrawal surname and gotra)",
            "Mohil/Mahiwal (Rajput gotra)",
            "Mudgalya/Maudgalya/Mudgal (Brahmin gotra)",
            "Munshi (Kashmiri pundits)",
            "Nanda (surname and gotra)",
            "Panchal (artisans of South India)",
            "Parashar (Brahmin gotra)",
            "Rathod (Rajput gotra)",
            "Rawal (Rajput, Gurjar, Brahmin and other gotras)",
            "Sandilya (Brahmin gotra)",
            "Saraswat (Brahmin gotra)",
            "Savarna (Kanyakubja Brahmin gotra)",
            "Shandilya (Brahmin gotra)",
            "Shaktri/Shakti (Vaidyabrahmin gotra)",
            "Sheoran (surname and gotra)",
            "Singhal (surname and gotra)",
            "Srivatsa (Brahmin gotra)",
            "Toppo (Kshatriya gotra)",
            "Upreti (Kumaoni Brahmin gotra)",
            "Vaid (Mohyal Brahmin gotra)",
            "Vasishtha (Brahmin gotra)",
            "Vats (clan) (Brahmin gotra)",
            "Vishvakarman (Vishwakarma Brahmin gotra)"};

    String[] Fvalue={"Traditional","Moderate","Liberal"};
    String[] Ftype={"Joint","Nuclear"};
    String[] Fstatus={"Affluent","Upper Middle Class","Middle Class","Lower Middle Class"};
    String[] Occupation={"Bussiness","Private","Goverment/Public","Defence","Civil Services","Other"};
    List<String> IncomeAnnual = new ArrayList<>();

    //carrer datas
    String[] HighestEducation={
            "Illiterate",
            "High School",
            "Intermediet",
            "B.Arc" ,
            "B.Des",
            "BE/B.TECH" ,
            "B.Pharma",
            "  M.Arch" ,
            "M.Des"  ,
            "M.E/M.Tech" ,
            "M.Pharma" ,
            "M.S(Engineering)" ,
            "Computer",
            "B.IT" ,
            "BCA",
            "MCA/PGDM" ,
            "Finance/Commerce:",
            "B.Com" ,
            "C.A",
            "CFA",
            "CS" ,
            "ICWA"  ,
            "M.Com",
            "Management",
            "BBA",
            "BHM" ,
            "MBA/PGDM" ,
            "Medicine:",
            "BAMS" ,
            "BDS",
            "BHMS" ,
            "BPT",
            "BVSc",
            "DM" ,
            "MD" ,
            "MS(Medicine)" ,
            "MBBS",
            "MCs" ,
            "MDS",
            "MPT",
            "MVSc" ,
            "LAW:",
            "BL/LLB" ,
            "ML/LLM" ,
            "ART SCIENCE:" ,
            "BA",
            "B.Ed" ,
            "B.Sc",
            "BFA" ,
            "BJMC" ,
            "M.A" ,
            "M.Ed" ,
            "M.Sc",
            "MFA",
            "MJMC",
            "MSW",
            "Doctorate:" ,
            "M.Phil" ,
            "Ph.D",
            "Other" };
    String[] UGdegree={
            "Don't Have",
            "B.Arc",
            "B.Des",
            "BE/B.TECH" ,
            "B.Pharma",
            "B.IT",
            "BCA",
            "B.IT",
            "BCA",
            "B.Com",
            "BBA",
            "BHM",
            "BAMS",
            "BDS",
            "BHMS" ,
            "BPT",
            "BVSc",
            "BL/LLB" ,
            "ML/LLM" ,
            "BA" ,
            "B.Ed",
            "B.Sc",
            "BFA",
            "BJMC",
    "MBBS",
    "Other"};
    String[] PgDegree={
            "Don't Have",
            "CFA" ,
            "CS" ,
            "DM"  ,
            "M.A" ,
            "M.Arch" ,
            "M.Com" ,
            "M.D." ,
            "M.Des" ,
            "M.E/M.Tech" ,
            "M.Ed" ,
            "M.Pharma" ,
            "M.Phil" ,
            "M.S(Engineering)" ,
            "M.S(Medicine)" ,
            "MBA/PGDM"  ,
            "MCA/PGDCA" ,
            "Mch",
            "MDS" ,
            "MFA" ,
            "MJMC" ,
            "ML/LLM",
            "MPT/MSW" ,
            "MVsc",
            "PhD" ,
            "Other"};
    String[] EmployedIn={"Not Working",
            "Admin Professional",
            "Clerk",
            "Operator/Technician",
            "Secretory/Front Office",
            "Civil Services",
            "Advertisement,Media,Entertainment  " ,
            "Advertisement Profession",
            "Film/Entertainment" ,
            "Media Professional" ,
            "PR Professional" ,
            "Agriculture  " ,
            "Agriculture Profession" ,
            "Farming" ,
            "Airline & Aviation  ",
            "Airline Profession" ,
            "Flight Attendant" ,
            "Pilot" ,
            "Architecture  " ,
            "Architect<" ,
            "BPO & Customer Services  "  ,
            "BPO/ITes Profession" ,
            "Customer Service" ,
            "Banking & Finance  " ,
            "Accounting Profession" ,
            "Auditor" ,
            "Banking Profession" ,
            "Chatered Accountant" ,
            "Finance Profession" ,
            "Corporate Management Professionals  ",
            "Consultant",
            "Corporate Communication",
            "Corporate Planning",
            "HR Profession",
            "Marketing Profession",
            "Operations Management" ,
            "Product Manager",
            "Program Manager" ,
            "Project Manager-IT",
            "Project Manager",
            "Sales Profession" ,
            "Sr. Manager/Manager" ,
            "Subject Matter Expert" ,
            "Doctor  " ,
            "Dentist" ,
            "Doctor" ,
            "Surgeon" ,
            "Education & Training  ",
            "ducation Profession" ,
            "Educational Institute Owner",
            "Librarian",
            "Professor/Lecturer",
            "Research Assistant" ,
            "Teacher",
            "Engineering  "  ,
            "Electronics Engineer",
            "Computer Engineering"  ,
            "Hardware/Telecom Engineer" ,
            "Non-IT Engineer" ,
            "Quality Assurance Engineer",
            "Hospitality  ",
            "Hotels/Hospitality" ,
            "Law Enforcement  " ,
            "Law Enforcement Officer" ,
            "Police" ,
            "Legal  "  ,
            "Lawer/Legal Professional",
            "Medical Healthcare & Other  "  ,
            "Medical Healthcare Professional" ,
            "Nurse",
            "Paramedic",
            "Pharmacist" ,
            "physiotherapist" ,
            "Psychologist",
            "Veterinary Doctor" ,
            "Merchant Navy  " ,
            "Mariner",
            "Merchant Naval Officer" ,
            "Reseach Professional",
            "Science Professional" ,
            "Scientist",
            "Software & IT  "  ,
            "Animator" ,
            "Cyber/Network Security",
            "Project Lead-IT",
            "Quality Asureance Engineer-IT" ,
            "Software Professional" ,
            "UI/UX Desiner" ,
            "Web/Graphics Designer" ,
            "Top Management  " ,
            "CxO/Chaiman/President/Director",
            "VP/AVP/GM/DGM",

            "Agent" ,
            "Artist" ,
            "Broker" ,
            "Fitness Professional"  ,
            "Interiro Designer" ,
            "Politician" ,
            "Security Professional" ,
            "Social Service/NGO/Volunteer"  ,
            "Others"};
    //end career datas

    String type;
    Button update;
    Detail detail;
    String token;
    static Dropdown dropdown;

    private DrService drService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);


        drService = ApiUtils.getClient().create(DrService.class);

        etAcaste = findViewById(R.id.a_caste);
        etAreligion = findViewById(R.id.a_religion);
        etAheight = findViewById(R.id.height);
        etAmartialStatus = findViewById(R.id.a_martial_status);
        etAmotherTounge = findViewById(R.id.mother_tounge);
        etAProfileFor = findViewById(R.id.profilr_managed_by);
        etAcomplexion = findViewById(R.id.complexion);
        etAbodyType = findViewById(R.id.body_type);
        etAbout=findViewById(R.id.a_about);

        sGotra=findViewById(R.id.a_sgotra);
        sMamagotra=findViewById(R.id.a_mgotra);
        sNaniGotra=findViewById(R.id.a_ngotra);
        sManglik=findViewById(R.id.a_manglik);

        etdHeShouldBe = findViewById(R.id.heshouldbe);
        etdAgeofAround = findViewById(R.id.ageofaround);
        etdMartialStatus = findViewById(R.id.martialstatus);
        etdChallenged = findViewById(R.id.challenged);
        etdReligion = findViewById(R.id.religion);
        etdMotherTounge = findViewById(R.id.mothertounge);
        etdCaste = findViewById(R.id.caste);
        etdCity = findViewById(R.id.dcity);
        etdCountry = findViewById(R.id.dcountry);
        etdEducation = findViewById(R.id.deducation);
        etHeducation = findViewById(R.id.heducation);
        etUgdegree = findViewById(R.id.ugdegree);
        etUgcollege = findViewById(R.id.ugcollege);
        etPgdegree = findViewById(R.id.pgdegree);
        etPgcollege = findViewById(R.id.pgcollege);
        etcHighesQualification = findViewById(R.id.highestqualification);
        etcEmployedIn = findViewById(R.id.employedin);
        etcAnnualIncome = findViewById(R.id.annualincome);
        etFtype = findViewById(R.id.ftype);
        etFvalue = findViewById(R.id.fvalue);
        etFstatus = findViewById(R.id.fstatus);
        etFincome = findViewById(R.id.fincome);
        etFoccupation = findViewById(R.id.foccupation);
        etFbrothers = findViewById(R.id.brother);
        etFsisters = findViewById(R.id.sister);
        update = findViewById(R.id.update_btn);
        aboutRl = findViewById(R.id.about_rl);

        familyRl=  findViewById(R.id.family_rl);
        desireRl = findViewById(R.id.derire_rl);
        educationRl = findViewById(R.id.education_rl);

        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        int layout=intent.getIntExtra("layout",0);

        if (layout==0){
            aboutRl.setVisibility(View.VISIBLE);
        }else if (layout==1){
            familyRl.setVisibility(View.VISIBLE);
        }else if (layout==2){
            educationRl.setVisibility(View.VISIBLE);
        }else if (layout==3){
            desireRl.setVisibility(View.VISIBLE);
        }
        else{

        }








        for (int i=4;i<8;i++){
            for (int j=0;j<12;j++){
                String h=i+" feet "+j+" inch";
            Height.add(h);
            }
        }


            for (int j=0;j<98;j++){
                String h="Rs "+j+" - "+(j+1)+" Lakh";
                IncomeAnnual.add(h);
            }
            IncomeAnnual.add("Above 1 Caror");

        for (int j=18;j<50;j=j=j+3){
            String k=j+"yrs - "+(j+3)+" yrs";
            AgeAround.add(k);
        }

        getDropdownData(drService,new SharedPreferenceUtil(this));






        getData(token);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Helper.getPaid(new SharedPreferenceUtil(AddDetailActivity.this))) {
                    getupdatedata();
                }else{
                    new SharedPreferenceUtil(AddDetailActivity.this).setPayViewType(0);
                    startActivity(new Intent(AddDetailActivity.this, PaymentActivity.class));
                }
            }
        });
    }


    private void setDropDowns(Dropdown dropdown) {

        adapterReligion= new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Religion);
        adapterReligion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAreligion.setAdapter(adapterReligion);

        adapterCaste=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Casted);
        adapterCaste.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAcaste.setAdapter(adapterCaste);

        adapterHeight=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Height);
        adapterHeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAheight.setAdapter(adapterHeight);

        adapterMartialStatus=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Mstatus);
        adapterMartialStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAmartialStatus.setAdapter(adapterMartialStatus);

        adapterProfileFor=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,ProfileFor);
        adapterProfileFor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAProfileFor.setAdapter(adapterProfileFor);

        adapterComplexion=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Complexion);
        adapterComplexion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAcomplexion.setAdapter(adapterComplexion);

        adapterAbodyType=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Bodytype);
        adapterAbodyType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAbodyType.setAdapter(adapterAbodyType);

        adapterSgotra=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Gotra);
        adapterSgotra.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sGotra.setAdapter(adapterSgotra);

        adapterNgotra=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Gotra);
        adapterNgotra.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sNaniGotra.setAdapter(adapterNgotra);

        adapterMgotra=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Gotra);
        adapterMgotra.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMamagotra.setAdapter(adapterMgotra);

        adapterManglik=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Manglik);
        adapterManglik.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sManglik.setAdapter(adapterManglik);

        adapterMotherTounge=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,MotherTounge);
        adapterMotherTounge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAmotherTounge.setAdapter(adapterMotherTounge);


        String selectedAge  = etAreligion.getSelectedItem().toString();
        //family adapters
        adapterFvalue=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Fvalue);
        adapterFvalue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etFvalue.setAdapter(adapterFvalue);

        adapterFtype=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Ftype);
        adapterFtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etFtype.setAdapter(adapterFtype);

        adapterFoccupation=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Occupation);
        adapterFoccupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etFoccupation.setAdapter(adapterFoccupation);

        adapterFstatus=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Fstatus);
        adapterFstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etFstatus.setAdapter(adapterFstatus);

        adapterFincome=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,IncomeAnnual);
        adapterFincome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etFincome.setAdapter(adapterFincome);

        //education
        adapteretHeducation=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,HighestEducation);
        adapteretHeducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etHeducation.setAdapter(adapteretHeducation);

        adapteretUgdegree=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,UGdegree);
        adapteretUgdegree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etUgdegree.setAdapter(adapteretUgdegree);

        adapterPgdegree=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,PgDegree);
        adapterPgdegree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etPgdegree.setAdapter(adapterPgdegree);

        adapterEmployedIn=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,EmployedIn);
        adapterEmployedIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etcEmployedIn.setAdapter(adapterEmployedIn);

        adaptercAnnualIncome=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,IncomeAnnual);
        adaptercAnnualIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etcAnnualIncome.setAdapter(adaptercAnnualIncome);

          //end education
         //Adapter Desired partner
        //ArrayAdapter<String>   adapterdMotherTounge, adapterdCaste, adapterdCity;
       // ArrayAdapter<CharSequence> adapterdAgeofAround, adapterdMartialStatus,  adapterdChallenged,  adapterdReligion, adapterdCountry;

        adapterdAgeofAround=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,AgeAround);
        adapterdAgeofAround.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdAgeofAround.setAdapter(adapterdAgeofAround);

        adapterdMartialStatus=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Mstatus);
        adapterdMartialStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdMartialStatus.setAdapter(adapterdMartialStatus);

        adapterdChallenged=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,OptionYN);
        adapterdChallenged.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdChallenged.setAdapter(adapterdChallenged);

        adapterdReligion=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Religion);
        adapterdReligion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdReligion.setAdapter(adapterdReligion);

        adapterdCity=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,City);
        adapterdCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdCity.setAdapter(adapterdCity);

        adapterdMotherTounge=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,MotherTounge);
        adapterdMotherTounge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdMotherTounge.setAdapter(adapterdMotherTounge);

        adapterdCaste=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Casted);
        adapterdCaste.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdCaste.setAdapter(adapterdCaste);

        adapterdCountry=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,State);
        adapterdCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etdCountry.setAdapter(adapterdCountry);

    }

    private void getDropdownData(DrService drService, SharedPreferenceUtil sharedPreferenceUtil) {
        drService.getDropdown(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null)).enqueue(new Callback<Dropdown>() {
            @Override
            public void onResponse(Call<Dropdown> call, Response<Dropdown> response) {
                if (response.isSuccessful()){
                   dropdown=response.body();
                    Log.d("serajd","Dropdown Loaded");
                    if (dropdown!=null){
                        Log.d("serajd","d loaded");
                        for (int i=0;i<dropdown.getCaste().size();i++){
                            Log.d("serajc",""+dropdown.getCaste().get(i).getValue());
                            Casted.add(""+dropdown.getCaste().get(i).getValue());
                        }
                        for (int i=0;i<dropdown.getMothertounge().size();i++){
                            Log.d("serajc",""+dropdown.getCaste().get(i).getValue());
                            MotherTounge.add(""+dropdown.getMothertounge().get(i).getValue());
                        }

                        for (int i = 0; i < dropdown.getCity().size(); i++) {
                            Log.d("serajc", "" + dropdown.getCity().get(i).getValue());
                            City.add("" + dropdown.getCity().get(i).getValue());
                        }

                        for (int i = 0; i < dropdown.getState().size(); i++) {
                            Log.d("serajc", "" + dropdown.getState().get(i).getValue());
                            State.add("" + dropdown.getState().get(i).getValue());
                        }
                    }

                    setDropDowns(dropdown);
                }
            }

            @Override
            public void onFailure(Call<Dropdown> call, Throwable t) {
                Log.d("serajd","Dropdown fail");
            }
        });
    }

    private void getData(String token1) {
        drService.showdetail(token1,"no").enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                detail=response.body();
                Log.d("Seraj","Data fetched");
                if (detail!=null){
                    Log.d("Seraj","Data not null");
                    setData(detail);
                    type="update";

                }else{
                    type="create";
                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(AddDetailActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        getData(token);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        getData(token);
        super.onRestart();
    }



    private void getupdatedata() {
        String dheshouldbe = etdHeShouldBe.getText().toString();
        String dageofaround = etdAgeofAround.getSelectedItem().toString();
        String dmartialstatus = etdMartialStatus.getSelectedItem().toString();
        String dchallenged = etdChallenged.getSelectedItem().toString();
        String dreligion = etdReligion.getSelectedItem().toString();
        String dmothertounge = etdMotherTounge.getSelectedItem().toString();
        String dcaste = etdCaste.getSelectedItem().toString();
        String dcity = etdCity.getSelectedItem().toString();
        String dcountry = etdCountry.getSelectedItem().toString();
        String deducation = etdEducation.getText().toString();
        String ehighest = etHeducation.getSelectedItem().toString();
        String eugdegree = etUgdegree.getSelectedItem().toString();
        String eugcollege = etUgcollege.getText().toString();
        String epgcollege =etPgcollege.getText().toString();
        String epgdegree = etPgdegree.getSelectedItem().toString();
        String chighesteducation = etcHighesQualification.getText().toString();
        String cemployedin = etcEmployedIn.getSelectedItem().toString();
        String cannuanincome = etcAnnualIncome.getSelectedItem().toString();
        String ftype = etFtype.getSelectedItem().toString();
        String fvalue = etFvalue.getSelectedItem().toString();
        String fstatus = etFstatus.getSelectedItem().toString();
        String fincome = etFincome.getSelectedItem().toString();
        String foccupation = etFoccupation.getSelectedItem().toString();
        String fbrother = etFbrothers.getText().toString();
        String fsister = etFsisters.getText().toString();
         String aCaste=etAcaste.getSelectedItem().toString();
        String aReligion=etAreligion.getSelectedItem().toString();//etAreligion.getText().toString();
        String aHeight =etAheight.getSelectedItem().toString();//etAheight.getText().toString();
        String aMartialStatus=etAmartialStatus.getSelectedItem().toString();//etAmartialStatus.getText().toString();
        String aMotherTounge=etAmotherTounge.getSelectedItem().toString();//etAmotherTounge.getText().toString();
        String aProfile=etAProfileFor.getSelectedItem().toString();//etAProfileFor.getText().toString();
        String complexion=etAcomplexion.getSelectedItem().toString();//etAcomplexion.getText().toString();
        String bodyTyoe=etAbodyType.getSelectedItem().toString();//etAbodyType.getText().toString();
        String selfgotra=sGotra.getSelectedItem().toString();
        String nanigotra=sNaniGotra.getSelectedItem().toString();
        String mamagotra=sMamagotra.getSelectedItem().toString();
        String manglik=sManglik.getSelectedItem().toString();
        String about=etAbout.getText().toString();

        updateNow(dheshouldbe, dageofaround, dmartialstatus, dchallenged, dreligion, dmothertounge, dcaste, dcity, dcountry, deducation, ehighest, eugdegree, eugcollege,
                epgcollege, epgdegree, chighesteducation, cemployedin, cannuanincome, ftype, fstatus, fvalue, fincome, foccupation, fbrother, fsister,aCaste
        ,aReligion,aHeight,aMartialStatus,aMotherTounge,aProfile,complexion,bodyTyoe,about,selfgotra,nanigotra,mamagotra,manglik);

    }

    private void updateNow(String dheshouldbe, String dageofaround, String dmartialstatus, String dchallenged, String dreligion, String dmothertounge, String dcaste, String dcity, String dcountry, String deducation, String ehighest, String eugdegree, String eugcollege, String epgcollege, String epgdegree, String chighesteducation, String cemployedin, String cannuanincome, String ftype, String fstatus, String fvalue, String fincome, String foccupation, String fbrother, String fsister, String aCaste, String aReligion, String aHeight, String aMartialStatus, String aMotherTounge, String aProfile, String complexion, String bodyTyoe, String about,String selfg,String nanig,String mamag,String manglik) {

        drService.detail(token, dheshouldbe, dageofaround, dmartialstatus, dchallenged, dreligion, dmothertounge, dcaste, dcity, dcountry, deducation, ehighest, eugdegree, eugcollege, epgcollege, epgdegree, chighesteducation, cemployedin,
                cannuanincome, ftype, fvalue, fstatus, fincome, foccupation, fbrother, fsister,
                aCaste,aReligion,aHeight,aMartialStatus,aMotherTounge,aProfile,complexion,bodyTyoe,about,selfg,nanig,mamag,manglik,"create").enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
              if (response.isSuccessful()){
                  detail=response.body();
                  Log.d("serajadddetail",detail.toString());
                  setData(detail);
                  Toast.makeText(AddDetailActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                  finish();
              }else{
                  try {
                      Log.d("serajadddetail",response.errorBody().string());
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(AddDetailActivity.this, "Failed to update please check your network connection...", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setData(Detail data) {
        Log.d("Seraj","Data setting");
        Log.d("Seraj",data.getBodytype()+"");
        etdHeShouldBe.setText(data.getDheshouldbe());
        etdAgeofAround.setSelection(listindexof(""+data.getDageofaround(),AgeAround));
        etdMartialStatus.setSelection(indexof(""+data.getDmartialstatus(),Mstatus));
        etdChallenged.setSelection(indexof(""+data.getDchallenged(),OptionYN));
        etdReligion.setSelection(indexof(""+data.getDreligion(),Religion));
        etdMotherTounge.setSelection(listindexof(""+data.getDmothertounge(),MotherTounge));
        etdCaste.setSelection(listindexof(""+data.getDcaste(),Casted));
        etdCity.setSelection(listindexof(""+data.getDcity(),City));
        etdCountry.setSelection(listindexof(""+data.getDcountry(),State));
        etdEducation.setText(data.getDeducation());
        etHeducation.setSelection(indexof(""+data.getEhighest(),HighestEducation));
        etUgdegree.setSelection(indexof(""+data.getEugdegree(),UGdegree));
        etUgcollege.setText(data.getEugcollege());
        etPgdegree.setSelection(indexof(""+data.getEpgdegree(),PgDegree));
        etPgcollege.setText(data.getEpgcollege());
        etcHighesQualification.setText(data.getChighesteducation());
        etcEmployedIn.setSelection(indexof(""+data.getCemployedin(),EmployedIn));
        etcAnnualIncome.setSelection(listindexof(""+data.getCannuanincome(),IncomeAnnual));
        etFtype.setSelection(indexof(""+data.getFtype(),Ftype));
        etFvalue.setSelection(indexof(""+data.getFvalue(),Fvalue));
        etFstatus.setSelection(indexof(""+data.getFstatus(),Fstatus));
        etFincome.setSelection(listindexof(""+data.getFincome(),IncomeAnnual));
        etFoccupation.setSelection(indexof(""+data.getFoccupation(),Occupation));
        etFbrothers.setText(data.getFbrother());
        etFsisters.setText(data.getFsister());
        etAcaste.setSelection(listindexof(""+data.getCaste(),Casted));       //setText(""+data.getCaste());
        etAreligion.setSelection(indexof(""+data.getReligion(),Religion));
        etAheight.setSelection(listindexof(""+data.getHeight(),Height));
        etAmartialStatus.setSelection(indexof(""+data.getMartialStatus(),Mstatus));
        etAmotherTounge.setSelection(listindexof(""+data.getMotherTounge(),MotherTounge));
        etAProfileFor.setSelection(indexof(""+data.getProfilefor(),ProfileFor));
        etAcomplexion.setSelection(indexof(""+data.getComplexion(),Complexion));
        etAbodyType.setSelection(indexof(""+data.getBodytype(),Bodytype));

        sGotra.setSelection(indexof(""+data.getSelfgotra(),Gotra));
        sNaniGotra.setSelection(indexof(""+data.getNanigotra(),Gotra));
        sMamagotra.setSelection(indexof(""+data.getMamagotra(),Gotra));
        sManglik.setSelection(indexof(""+data.getManglik(),Manglik));
        etAreligion.setSelection(indexof(""+data.getReligion(),Religion));
        etAbout.setText(data.getAboutme());

    }

    private int indexof(String s, String[] TYPES) {
        int index = -1;
        for (int i=0;i<TYPES.length;i++) {
            if (TYPES[i].equalsIgnoreCase(s)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int listindexof(String s, List<String> TYPES) {
        int index = -1;
        for (int i=0;i<TYPES.size();i++) {
            if (TYPES.get(i).equalsIgnoreCase(s)) {
                index = i;
                break;
            }
        }
        return index;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}