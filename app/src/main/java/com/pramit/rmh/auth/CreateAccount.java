package com.pramit.rmh.auth;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.pramit.rmh.AWSConnection;
import com.pramit.rmh.R;
import com.pramit.rmh.UIUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.time.temporal.ChronoField.*;

public class CreateAccount extends AppCompatActivity {
    private static final Map<ArrayList<String>, Integer> countryToCodes = new HashMap<>();
    private static final DateTimeFormatter naturalDateTime;
    private static final DateTimeFormatter awsDateTime = DateTimeFormatter.ofPattern("MM/dd/uuuu", Locale.US);
    private static final long AGE_LIMIT_MILLIS = 567648000000L;
    private static final long LOWER_AGE_BOUND_OFFSET = 3784320000000L;

    static {
        ArrayList<String> listWithRegionCode;

        listWithRegionCode = new ArrayList<>(25);
        listWithRegionCode.add("US");
        listWithRegionCode.add("AG");
        listWithRegionCode.add("AI");
        listWithRegionCode.add("AS");
        listWithRegionCode.add("BB");
        listWithRegionCode.add("BM");
        listWithRegionCode.add("BS");
        listWithRegionCode.add("CA");
        listWithRegionCode.add("DM");
        listWithRegionCode.add("DO");
        listWithRegionCode.add("GD");
        listWithRegionCode.add("GU");
        listWithRegionCode.add("JM");
        listWithRegionCode.add("KN");
        listWithRegionCode.add("KY");
        listWithRegionCode.add("LC");
        listWithRegionCode.add("MP");
        listWithRegionCode.add("MS");
        listWithRegionCode.add("PR");
        listWithRegionCode.add("SX");
        listWithRegionCode.add("TC");
        listWithRegionCode.add("TT");
        listWithRegionCode.add("VC");
        listWithRegionCode.add("VG");
        listWithRegionCode.add("VI");
        countryToCodes.put(listWithRegionCode, 1);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("RU");
        listWithRegionCode.add("KZ");
        countryToCodes.put(listWithRegionCode, 7);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("EG");
        countryToCodes.put(listWithRegionCode, 20);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ZA");
        countryToCodes.put(listWithRegionCode, 27);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GR");
        countryToCodes.put(listWithRegionCode, 30);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NL");
        countryToCodes.put(listWithRegionCode, 31);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BE");
        countryToCodes.put(listWithRegionCode, 32);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("FR");
        countryToCodes.put(listWithRegionCode, 33);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ES");
        countryToCodes.put(listWithRegionCode, 34);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("HU");
        countryToCodes.put(listWithRegionCode, 36);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("IT");
        listWithRegionCode.add("VA");
        countryToCodes.put(listWithRegionCode, 39);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("RO");
        countryToCodes.put(listWithRegionCode, 40);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CH");
        countryToCodes.put(listWithRegionCode, 41);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AT");
        countryToCodes.put(listWithRegionCode, 43);

        listWithRegionCode = new ArrayList<>(4);
        listWithRegionCode.add("GB");
        listWithRegionCode.add("GG");
        listWithRegionCode.add("IM");
        listWithRegionCode.add("JE");
        countryToCodes.put(listWithRegionCode, 44);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("DK");
        countryToCodes.put(listWithRegionCode, 45);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SE");
        countryToCodes.put(listWithRegionCode, 46);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("NO");
        listWithRegionCode.add("SJ");
        countryToCodes.put(listWithRegionCode, 47);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PL");
        countryToCodes.put(listWithRegionCode, 48);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("DE");
        countryToCodes.put(listWithRegionCode, 49);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PE");
        countryToCodes.put(listWithRegionCode, 51);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MX");
        countryToCodes.put(listWithRegionCode, 52);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CU");
        countryToCodes.put(listWithRegionCode, 53);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AR");
        countryToCodes.put(listWithRegionCode, 54);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BR");
        countryToCodes.put(listWithRegionCode, 55);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CL");
        countryToCodes.put(listWithRegionCode, 56);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CO");
        countryToCodes.put(listWithRegionCode, 57);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("VE");
        countryToCodes.put(listWithRegionCode, 58);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MY");
        countryToCodes.put(listWithRegionCode, 60);

        listWithRegionCode = new ArrayList<>(3);
        listWithRegionCode.add("AU");
        listWithRegionCode.add("CC");
        listWithRegionCode.add("CX");
        countryToCodes.put(listWithRegionCode, 61);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ID");
        countryToCodes.put(listWithRegionCode, 62);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PH");
        countryToCodes.put(listWithRegionCode, 63);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NZ");
        countryToCodes.put(listWithRegionCode, 64);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SG");
        countryToCodes.put(listWithRegionCode, 65);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TH");
        countryToCodes.put(listWithRegionCode, 66);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("JP");
        countryToCodes.put(listWithRegionCode, 81);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KR");
        countryToCodes.put(listWithRegionCode, 82);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("VN");
        countryToCodes.put(listWithRegionCode, 84);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CN");
        countryToCodes.put(listWithRegionCode, 86);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TR");
        countryToCodes.put(listWithRegionCode, 90);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("IN");
        countryToCodes.put(listWithRegionCode, 91);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PK");
        countryToCodes.put(listWithRegionCode, 92);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AF");
        countryToCodes.put(listWithRegionCode, 93);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LK");
        countryToCodes.put(listWithRegionCode, 94);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MM");
        countryToCodes.put(listWithRegionCode, 95);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("IR");
        countryToCodes.put(listWithRegionCode, 98);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SS");
        countryToCodes.put(listWithRegionCode, 211);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("MA");
        listWithRegionCode.add("EH");
        countryToCodes.put(listWithRegionCode, 212);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("DZ");
        countryToCodes.put(listWithRegionCode, 213);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TN");
        countryToCodes.put(listWithRegionCode, 216);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LY");
        countryToCodes.put(listWithRegionCode, 218);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GM");
        countryToCodes.put(listWithRegionCode, 220);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SN");
        countryToCodes.put(listWithRegionCode, 221);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MR");
        countryToCodes.put(listWithRegionCode, 222);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ML");
        countryToCodes.put(listWithRegionCode, 223);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GN");
        countryToCodes.put(listWithRegionCode, 224);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CI");
        countryToCodes.put(listWithRegionCode, 225);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BF");
        countryToCodes.put(listWithRegionCode, 226);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NE");
        countryToCodes.put(listWithRegionCode, 227);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TG");
        countryToCodes.put(listWithRegionCode, 228);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BJ");
        countryToCodes.put(listWithRegionCode, 229);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MU");
        countryToCodes.put(listWithRegionCode, 230);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LR");
        countryToCodes.put(listWithRegionCode, 231);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SL");
        countryToCodes.put(listWithRegionCode, 232);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GH");
        countryToCodes.put(listWithRegionCode, 233);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NG");
        countryToCodes.put(listWithRegionCode, 234);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TD");
        countryToCodes.put(listWithRegionCode, 235);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CF");
        countryToCodes.put(listWithRegionCode, 236);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CM");
        countryToCodes.put(listWithRegionCode, 237);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CV");
        countryToCodes.put(listWithRegionCode, 238);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ST");
        countryToCodes.put(listWithRegionCode, 239);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GQ");
        countryToCodes.put(listWithRegionCode, 240);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GA");
        countryToCodes.put(listWithRegionCode, 241);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CG");
        countryToCodes.put(listWithRegionCode, 242);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CD");
        countryToCodes.put(listWithRegionCode, 243);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AO");
        countryToCodes.put(listWithRegionCode, 244);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GW");
        countryToCodes.put(listWithRegionCode, 245);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("IO");
        countryToCodes.put(listWithRegionCode, 246);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AC");
        countryToCodes.put(listWithRegionCode, 247);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SC");
        countryToCodes.put(listWithRegionCode, 248);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SD");
        countryToCodes.put(listWithRegionCode, 249);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("RW");
        countryToCodes.put(listWithRegionCode, 250);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ET");
        countryToCodes.put(listWithRegionCode, 251);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SO");
        countryToCodes.put(listWithRegionCode, 252);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("DJ");
        countryToCodes.put(listWithRegionCode, 253);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KE");
        countryToCodes.put(listWithRegionCode, 254);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TZ");
        countryToCodes.put(listWithRegionCode, 255);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("UG");
        countryToCodes.put(listWithRegionCode, 256);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BI");
        countryToCodes.put(listWithRegionCode, 257);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MZ");
        countryToCodes.put(listWithRegionCode, 258);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ZM");
        countryToCodes.put(listWithRegionCode, 260);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MG");
        countryToCodes.put(listWithRegionCode, 261);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("RE");
        listWithRegionCode.add("YT");
        countryToCodes.put(listWithRegionCode, 262);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ZW");
        countryToCodes.put(listWithRegionCode, 263);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NA");
        countryToCodes.put(listWithRegionCode, 264);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MW");
        countryToCodes.put(listWithRegionCode, 265);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LS");
        countryToCodes.put(listWithRegionCode, 266);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BW");
        countryToCodes.put(listWithRegionCode, 267);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SZ");
        countryToCodes.put(listWithRegionCode, 268);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KM");
        countryToCodes.put(listWithRegionCode, 269);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("SH");
        listWithRegionCode.add("TA");
        countryToCodes.put(listWithRegionCode, 290);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ER");
        countryToCodes.put(listWithRegionCode, 291);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AW");
        countryToCodes.put(listWithRegionCode, 297);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("FO");
        countryToCodes.put(listWithRegionCode, 298);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GL");
        countryToCodes.put(listWithRegionCode, 299);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GI");
        countryToCodes.put(listWithRegionCode, 350);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PT");
        countryToCodes.put(listWithRegionCode, 351);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LU");
        countryToCodes.put(listWithRegionCode, 352);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("IE");
        countryToCodes.put(listWithRegionCode, 353);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("IS");
        countryToCodes.put(listWithRegionCode, 354);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AL");
        countryToCodes.put(listWithRegionCode, 355);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MT");
        countryToCodes.put(listWithRegionCode, 356);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CY");
        countryToCodes.put(listWithRegionCode, 357);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("FI");
        listWithRegionCode.add("AX");
        countryToCodes.put(listWithRegionCode, 358);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BG");
        countryToCodes.put(listWithRegionCode, 359);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LT");
        countryToCodes.put(listWithRegionCode, 370);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LV");
        countryToCodes.put(listWithRegionCode, 371);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("EE");
        countryToCodes.put(listWithRegionCode, 372);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MD");
        countryToCodes.put(listWithRegionCode, 373);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AM");
        countryToCodes.put(listWithRegionCode, 374);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BY");
        countryToCodes.put(listWithRegionCode, 375);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AD");
        countryToCodes.put(listWithRegionCode, 376);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MC");
        countryToCodes.put(listWithRegionCode, 377);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SM");
        countryToCodes.put(listWithRegionCode, 378);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("UA");
        countryToCodes.put(listWithRegionCode, 380);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("RS");
        countryToCodes.put(listWithRegionCode, 381);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("ME");
        countryToCodes.put(listWithRegionCode, 382);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("XK");
        countryToCodes.put(listWithRegionCode, 383);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("HR");
        countryToCodes.put(listWithRegionCode, 385);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SI");
        countryToCodes.put(listWithRegionCode, 386);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BA");
        countryToCodes.put(listWithRegionCode, 387);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MK");
        countryToCodes.put(listWithRegionCode, 389);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CZ");
        countryToCodes.put(listWithRegionCode, 420);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SK");
        countryToCodes.put(listWithRegionCode, 421);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LI");
        countryToCodes.put(listWithRegionCode, 423);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("FK");
        countryToCodes.put(listWithRegionCode, 500);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BZ");
        countryToCodes.put(listWithRegionCode, 501);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GT");
        countryToCodes.put(listWithRegionCode, 502);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SV");
        countryToCodes.put(listWithRegionCode, 503);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("HN");
        countryToCodes.put(listWithRegionCode, 504);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NI");
        countryToCodes.put(listWithRegionCode, 505);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CR");
        countryToCodes.put(listWithRegionCode, 506);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PA");
        countryToCodes.put(listWithRegionCode, 507);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PM");
        countryToCodes.put(listWithRegionCode, 508);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("HT");
        countryToCodes.put(listWithRegionCode, 509);

        listWithRegionCode = new ArrayList<>(3);
        listWithRegionCode.add("GP");
        listWithRegionCode.add("BL");
        listWithRegionCode.add("MF");
        countryToCodes.put(listWithRegionCode, 590);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BO");
        countryToCodes.put(listWithRegionCode, 591);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GY");
        countryToCodes.put(listWithRegionCode, 592);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("EC");
        countryToCodes.put(listWithRegionCode, 593);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GF");
        countryToCodes.put(listWithRegionCode, 594);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PY");
        countryToCodes.put(listWithRegionCode, 595);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MQ");
        countryToCodes.put(listWithRegionCode, 596);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SR");
        countryToCodes.put(listWithRegionCode, 597);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("UY");
        countryToCodes.put(listWithRegionCode, 598);

        listWithRegionCode = new ArrayList<>(2);
        listWithRegionCode.add("CW");
        listWithRegionCode.add("BQ");
        countryToCodes.put(listWithRegionCode, 599);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TL");
        countryToCodes.put(listWithRegionCode, 670);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NF");
        countryToCodes.put(listWithRegionCode, 672);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BN");
        countryToCodes.put(listWithRegionCode, 673);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NR");
        countryToCodes.put(listWithRegionCode, 674);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PG");
        countryToCodes.put(listWithRegionCode, 675);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TO");
        countryToCodes.put(listWithRegionCode, 676);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SB");
        countryToCodes.put(listWithRegionCode, 677);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("VU");
        countryToCodes.put(listWithRegionCode, 678);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("FJ");
        countryToCodes.put(listWithRegionCode, 679);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PW");
        countryToCodes.put(listWithRegionCode, 680);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("WF");
        countryToCodes.put(listWithRegionCode, 681);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("CK");
        countryToCodes.put(listWithRegionCode, 682);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NU");
        countryToCodes.put(listWithRegionCode, 683);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("WS");
        countryToCodes.put(listWithRegionCode, 685);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KI");
        countryToCodes.put(listWithRegionCode, 686);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NC");
        countryToCodes.put(listWithRegionCode, 687);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TV");
        countryToCodes.put(listWithRegionCode, 688);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PF");
        countryToCodes.put(listWithRegionCode, 689);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TK");
        countryToCodes.put(listWithRegionCode, 690);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("FM");
        countryToCodes.put(listWithRegionCode, 691);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MH");
        countryToCodes.put(listWithRegionCode, 692);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 800);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 808);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KP");
        countryToCodes.put(listWithRegionCode, 850);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("HK");
        countryToCodes.put(listWithRegionCode, 852);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MO");
        countryToCodes.put(listWithRegionCode, 853);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KH");
        countryToCodes.put(listWithRegionCode, 855);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LA");
        countryToCodes.put(listWithRegionCode, 856);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 870);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 878);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BD");
        countryToCodes.put(listWithRegionCode, 880);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 881);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 882);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 883);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TW");
        countryToCodes.put(listWithRegionCode, 886);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 888);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MV");
        countryToCodes.put(listWithRegionCode, 960);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("LB");
        countryToCodes.put(listWithRegionCode, 961);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("JO");
        countryToCodes.put(listWithRegionCode, 962);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SY");
        countryToCodes.put(listWithRegionCode, 963);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("IQ");
        countryToCodes.put(listWithRegionCode, 964);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KW");
        countryToCodes.put(listWithRegionCode, 965);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("SA");
        countryToCodes.put(listWithRegionCode, 966);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("YE");
        countryToCodes.put(listWithRegionCode, 967);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("OM");
        countryToCodes.put(listWithRegionCode, 968);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("PS");
        countryToCodes.put(listWithRegionCode, 970);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AE");
        countryToCodes.put(listWithRegionCode, 971);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("IL");
        countryToCodes.put(listWithRegionCode, 972);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BH");
        countryToCodes.put(listWithRegionCode, 973);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("QA");
        countryToCodes.put(listWithRegionCode, 974);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("BT");
        countryToCodes.put(listWithRegionCode, 975);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("MN");
        countryToCodes.put(listWithRegionCode, 976);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("NP");
        countryToCodes.put(listWithRegionCode, 977);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("001");
        countryToCodes.put(listWithRegionCode, 979);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TJ");
        countryToCodes.put(listWithRegionCode, 992);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("TM");
        countryToCodes.put(listWithRegionCode, 993);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("AZ");
        countryToCodes.put(listWithRegionCode, 994);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("GE");
        countryToCodes.put(listWithRegionCode, 995);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("KG");
        countryToCodes.put(listWithRegionCode, 996);

        listWithRegionCode = new ArrayList<>(1);
        listWithRegionCode.add("UZ");
        countryToCodes.put(listWithRegionCode, 998);

        Map<Long, String> moy = new HashMap<>();
        moy.put(1L, "January");
        moy.put(2L, "February");
        moy.put(3L, "March");
        moy.put(4L, "April");
        moy.put(5L, "May");
        moy.put(6L, "June");
        moy.put(7L, "July");
        moy.put(8L, "August");
        moy.put(9L, "September");
        moy.put(10L, "October");
        moy.put(11L, "November");
        moy.put(12L, "December");
        naturalDateTime = new DateTimeFormatterBuilder()
                .parseLenient()
                .parseCaseInsensitive()
                .appendText(MONTH_OF_YEAR, moy)
                .appendLiteral(' ')
                .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(", ")
                .appendValue(YEAR, 4)
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.SMART);
    }

    private AWSConnection aws;

    public static int getCountryAreaCode(String region) {
        for (Map.Entry<ArrayList<String>, Integer> countryCode : countryToCodes.entrySet()) {
            ArrayList<String> countries = countryCode.getKey();
            for (String country : countries)
                if (country.equals(region))
                    return countryCode.getValue();
        }
        return -1;
    }

    public static boolean validatePhone(String phoneNum) {
        return phoneNum.matches("\\+\\d{11,15}");
    }

    public static boolean validateEmail(String email) {
        return email.matches("(?:[a-z\\d!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z\\d!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z\\d](?:[a-z\\d-]*[a-z\\d])?\\.)+[a-z\\d](?:[a-z\\d-]*[a-z\\d])?|\\[(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?|[a-z\\d-]*[a-z\\d]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
    }

    public static String parseDate(String date) {
        LocalDateTime localDateTime = LocalDate.parse(date, naturalDateTime).atStartOfDay();
        return localDateTime.format(awsDateTime);
    }

    public static boolean validatePassword(String password) {
        return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^$*.\\[\\]{}()?\\-\"!@#%&/\\s,><':;|_~`+=])(?=\\S+$).{8,}$");
    }

    public static boolean validateAge(String birthday) {
        LocalDateTime todaysDate = LocalDateTime.now();
        LocalDateTime birthDayDate = LocalDate.parse(birthday, naturalDateTime).atStartOfDay();
        return birthDayDate.until(todaysDate, ChronoUnit.MILLIS) >= AGE_LIMIT_MILLIS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        this.aws = AWSConnection.getInstance(getApplicationContext());
        initListeners();
    }

    public void initListeners() {
        TextInputLayout phoneContainer = findViewById(R.id.phoneContainer);

        TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String region = telephonyManager.getSimCountryIso().toUpperCase(Locale.US);
        String countryCode = "+" + getCountryAreaCode(region);
        phoneContainer.setPrefixText(countryCode);
        TextView prefix = phoneContainer.getPrefixTextView();
        prefix.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        prefix.setGravity(Gravity.CENTER);

        MaterialDatePicker.Builder<Long> datePickerBuilder = UIUtils.createDateDialog(getApplicationContext(), R.string.birthday_lbl);
        CalendarConstraints calendarConstraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .setStart(System.currentTimeMillis() - LOWER_AGE_BOUND_OFFSET)
                .setOpenAt(MaterialDatePicker.todayInUtcMilliseconds())
                .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePickerBuilder.setCalendarConstraints(calendarConstraints);
        MaterialDatePicker<Long> datePicker = datePickerBuilder.build();
        datePicker.addOnPositiveButtonClickListener(dateSelected -> {
            Instant instant = Instant.ofEpochMilli(dateSelected);
            LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.of("Z"));
            ((EditText) findViewById(R.id.birthday)).setText(date.format(naturalDateTime));
        });

        EditText birthdayEditText = findViewById(R.id.birthday);
        birthdayEditText.setInputType(InputType.TYPE_NULL);
        birthdayEditText.setKeyListener(null);

        birthdayEditText.setOnClickListener(click -> datePicker.show(getSupportFragmentManager(), datePicker.toString()));
        birthdayEditText.setOnFocusChangeListener((view, isFocused) -> {
            if (isFocused)
                datePicker.show(getSupportFragmentManager(), datePicker.toString());
        });


        findViewById(R.id.register).setOnClickListener(e -> {
            if (validateFields()) {
                Map<String, String> registrationData = getRegistrationData();
                String userId = registrationData.get("preferred_username");
                String password = registrationData.remove("password");
                aws.signUp(this, userId, password, registrationData);
            }
        });
    }

    public Map<String, String> getRegistrationData() {
        String phoneCode = Objects.requireNonNull(((TextInputLayout) findViewById(R.id.phoneContainer)).getPrefixText()).toString();
        String phoneNum = ((EditText) (findViewById(R.id.phone))).getText().toString();
        String fullPhone = (phoneCode + phoneNum).replaceAll("[^+\\d]", "");
        HashMap<String, String> map = new HashMap<>();
        map.put("name", ((EditText) (findViewById(R.id.name))).getText().toString());
        map.put("preferred_username", ((EditText) (findViewById(R.id.username))).getText().toString());
        map.put("email", ((EditText) (findViewById(R.id.email))).getText().toString());
        map.put("birthdate", parseDate(((EditText) (findViewById(R.id.birthday))).getText().toString()));
        map.put("locale", Resources.getSystem().getConfiguration().getLocales().get(0).toString());
        map.put("phone_number", fullPhone);
        map.put("password", ((EditText) (findViewById(R.id.password))).getText().toString());
        return map;
    }

    //TODO: ADD UI ERROR NOTIFICATIONS
    public boolean validateFields() {
        String phoneCode = Objects.requireNonNull(((TextInputLayout) findViewById(R.id.phoneContainer)).getPrefixText()).toString();
        String phoneNum = ((EditText) (findViewById(R.id.phone))).getText().toString();
        String fullPhone = (phoneCode + phoneNum).replaceAll("[^+\\d]", "");
        if (!validatePhone(fullPhone))
            return false;
        String email = ((EditText) (findViewById(R.id.email))).getText().toString();
        if (!validateEmail(email))
            return false;
        String birthday = ((EditText) (findViewById(R.id.birthday))).getText().toString();
        if (!validateAge(birthday))
            return false;
        String userId = ((EditText) (findViewById(R.id.username))).getText().toString();
        if (userId.equals(""))
            return false;
        String password = ((EditText) (findViewById(R.id.password))).getText().toString();
        if (!validatePassword(password))
            return false;
        String passwordConfirmation = ((EditText) (findViewById(R.id.confirmPassword))).getText().toString();
        return passwordConfirmation.equals(password);
    }
}
