// $ANTLR 3.2 Sep 23, 2009 12:02:23 EsperEPL2Ast.g 2013-02-27 14:55:22

  package com.espertech.esper.epl.generated;
  import java.util.Stack;
  import org.apache.commons.logging.Log;
  import org.apache.commons.logging.LogFactory;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class EsperEPL2Ast extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CREATE", "WINDOW", "IN_SET", "BETWEEN", "LIKE", "REGEXP", "ESCAPE", "OR_EXPR", "AND_EXPR", "NOT_EXPR", "EVERY_EXPR", "EVERY_DISTINCT_EXPR", "WHERE", "AS", "SUM", "AVG", "MAX", "MIN", "COALESCE", "MEDIAN", "STDDEV", "AVEDEV", "COUNT", "SELECT", "CASE", "CASE2", "ELSE", "WHEN", "THEN", "END", "FROM", "OUTER", "INNER", "JOIN", "LEFT", "RIGHT", "FULL", "ON", "IS", "BY", "GROUP", "HAVING", "DISTINCT", "ALL", "ANY", "SOME", "OUTPUT", "EVENTS", "FIRST", "LAST", "INSERT", "INTO", "ORDER", "ASC", "DESC", "RSTREAM", "ISTREAM", "IRSTREAM", "SCHEMA", "UNIDIRECTIONAL", "RETAINUNION", "RETAININTERSECTION", "PATTERN", "SQL", "METADATASQL", "PREVIOUS", "PREVIOUSTAIL", "PREVIOUSCOUNT", "PREVIOUSWINDOW", "PRIOR", "EXISTS", "WEEKDAY", "LW", "INSTANCEOF", "TYPEOF", "CAST", "CURRENT_TIMESTAMP", "DELETE", "SNAPSHOT", "SET", "VARIABLE", "UNTIL", "AT", "INDEX", "TIMEPERIOD_YEAR", "TIMEPERIOD_YEARS", "TIMEPERIOD_MONTH", "TIMEPERIOD_MONTHS", "TIMEPERIOD_WEEK", "TIMEPERIOD_WEEKS", "TIMEPERIOD_DAY", "TIMEPERIOD_DAYS", "TIMEPERIOD_HOUR", "TIMEPERIOD_HOURS", "TIMEPERIOD_MINUTE", "TIMEPERIOD_MINUTES", "TIMEPERIOD_SEC", "TIMEPERIOD_SECOND", "TIMEPERIOD_SECONDS", "TIMEPERIOD_MILLISEC", "TIMEPERIOD_MILLISECOND", "TIMEPERIOD_MILLISECONDS", "BOOLEAN_TRUE", "BOOLEAN_FALSE", "VALUE_NULL", "ROW_LIMIT_EXPR", "OFFSET", "UPDATE", "MATCH_RECOGNIZE", "MEASURES", "DEFINE", "PARTITION", "MATCHES", "AFTER", "FOR", "WHILE", "USING", "MERGE", "MATCHED", "EXPRESSIONDECL", "NEWKW", "START", "CONTEXT", "INITIATED", "TERMINATED", "DATAFLOW", "NUMERIC_PARAM_RANGE", "NUMERIC_PARAM_LIST", "NUMERIC_PARAM_FREQUENCY", "OBJECT_PARAM_ORDERED_EXPR", "FOLLOWED_BY_EXPR", "FOLLOWED_BY_ITEM", "PATTERN_FILTER_EXPR", "PATTERN_NOT_EXPR", "PATTERN_EVERY_DISTINCT_EXPR", "EVENT_FILTER_EXPR", "EVENT_FILTER_PROPERTY_EXPR", "EVENT_FILTER_PROPERTY_EXPR_ATOM", "PROPERTY_SELECTION_ELEMENT_EXPR", "PROPERTY_SELECTION_STREAM", "PROPERTY_WILDCARD_SELECT", "EVENT_FILTER_IDENT", "EVENT_FILTER_PARAM", "EVENT_FILTER_RANGE", "EVENT_FILTER_NOT_RANGE", "EVENT_FILTER_IN", "EVENT_FILTER_NOT_IN", "EVENT_FILTER_BETWEEN", "EVENT_FILTER_NOT_BETWEEN", "CLASS_IDENT", "GUARD_EXPR", "OBSERVER_EXPR", "VIEW_EXPR", "PATTERN_INCL_EXPR", "DATABASE_JOIN_EXPR", "WHERE_EXPR", "HAVING_EXPR", "EVAL_BITWISE_EXPR", "EVAL_AND_EXPR", "EVAL_OR_EXPR", "EVAL_EQUALS_EXPR", "EVAL_NOTEQUALS_EXPR", "EVAL_IS_EXPR", "EVAL_ISNOT_EXPR", "EVAL_EQUALS_GROUP_EXPR", "EVAL_NOTEQUALS_GROUP_EXPR", "EVAL_IDENT", "SELECTION_EXPR", "SELECTION_ELEMENT_EXPR", "SELECTION_STREAM", "STREAM_EXPR", "OUTERJOIN_EXPR", "INNERJOIN_EXPR", "LEFT_OUTERJOIN_EXPR", "RIGHT_OUTERJOIN_EXPR", "FULL_OUTERJOIN_EXPR", "GROUP_BY_EXPR", "ORDER_BY_EXPR", "ORDER_ELEMENT_EXPR", "EVENT_PROP_EXPR", "EVENT_PROP_SIMPLE", "EVENT_PROP_MAPPED", "EVENT_PROP_INDEXED", "EVENT_PROP_DYNAMIC_SIMPLE", "EVENT_PROP_DYNAMIC_INDEXED", "EVENT_PROP_DYNAMIC_MAPPED", "EVENT_LIMIT_EXPR", "TIMEPERIOD_LIMIT_EXPR", "AFTER_LIMIT_EXPR", "CRONTAB_LIMIT_EXPR", "CRONTAB_LIMIT_EXPR_PARAM", "WHEN_LIMIT_EXPR", "TERM_LIMIT_EXPR", "INSERTINTO_EXPR", "EXPRCOL", "INDEXCOL", "CONCAT", "LIB_FUNCTION", "LIB_FUNC_CHAIN", "DOT_EXPR", "UNARY_MINUS", "TIME_PERIOD", "ARRAY_EXPR", "YEAR_PART", "MONTH_PART", "WEEK_PART", "DAY_PART", "HOUR_PART", "MINUTE_PART", "SECOND_PART", "MILLISECOND_PART", "NOT_IN_SET", "NOT_BETWEEN", "NOT_LIKE", "NOT_REGEXP", "DBSELECT_EXPR", "DBFROM_CLAUSE", "DBWHERE_CLAUSE", "WILDCARD_SELECT", "INSERTINTO_STREAM_NAME", "IN_RANGE", "NOT_IN_RANGE", "SUBSELECT_EXPR", "SUBSELECT_GROUP_EXPR", "EXISTS_SUBSELECT_EXPR", "IN_SUBSELECT_EXPR", "NOT_IN_SUBSELECT_EXPR", "IN_SUBSELECT_QUERY_EXPR", "LAST_OPERATOR", "WEEKDAY_OPERATOR", "SUBSTITUTION", "CAST_EXPR", "CREATE_INDEX_EXPR", "CREATE_WINDOW_EXPR", "CREATE_WINDOW_SELECT_EXPR", "ON_EXPR", "ON_STREAM", "ON_DELETE_EXPR", "ON_SELECT_EXPR", "ON_UPDATE_EXPR", "ON_MERGE_EXPR", "ON_SELECT_INSERT_EXPR", "ON_SELECT_INSERT_OUTPUT", "ON_EXPR_FROM", "ON_SET_EXPR", "CREATE_VARIABLE_EXPR", "METHOD_JOIN_EXPR", "MATCH_UNTIL_EXPR", "MATCH_UNTIL_RANGE_HALFOPEN", "MATCH_UNTIL_RANGE_HALFCLOSED", "MATCH_UNTIL_RANGE_CLOSED", "MATCH_UNTIL_RANGE_BOUNDED", "CREATE_COL_TYPE_LIST", "CREATE_COL_TYPE", "NUMBERSETSTAR", "ANNOTATION", "ANNOTATION_ARRAY", "ANNOTATION_VALUE", "FIRST_AGGREG", "LAST_AGGREG", "WINDOW_AGGREG", "ACCESS_AGG", "UPDATE_EXPR", "ON_SET_EXPR_ITEM", "CREATE_SCHEMA_EXPR", "CREATE_SCHEMA_EXPR_QUAL", "CREATE_SCHEMA_DEF", "VARIANT_LIST", "MERGE_UNM", "MERGE_MAT", "MERGE_UPD", "MERGE_INS", "MERGE_DEL", "NEW_ITEM", "AGG_FILTER_EXPR", "CREATE_EXPR", "CREATE_CTX", "CREATE_CTX_FIXED", "CREATE_CTX_PART", "CREATE_CTX_COAL", "CREATE_CTX_CAT", "CREATE_CTX_INIT", "CREATE_CTX_CATITEM", "CREATE_CTX_NESTED", "CREATE_CTX_PATTERN", "CREATE_DATAFLOW", "GOP", "GOPPARAM", "GOPPARAMITM", "GOPOUT", "GOPOUTITM", "GOPOUTTYP", "GOPCFG", "GOPCFGITM", "GOPCFGEPL", "PARTITIONITEM", "INT_TYPE", "LONG_TYPE", "FLOAT_TYPE", "DOUBLE_TYPE", "STRING_TYPE", "BOOL_TYPE", "NULL_TYPE", "NUM_DOUBLE", "EPL_EXPR", "MATCHREC_PATTERN", "MATCHREC_PATTERN_ATOM", "MATCHREC_PATTERN_CONCAT", "MATCHREC_PATTERN_ALTER", "MATCHREC_PATTERN_NESTED", "MATCHREC_AFTER_SKIP", "MATCHREC_INTERVAL", "MATCHREC_DEFINE", "MATCHREC_DEFINE_ITEM", "MATCHREC_MEASURES", "MATCHREC_MEASURE_ITEM", "JSON_OBJECT", "JSON_ARRAY", "JSON_FIELD", "LBRACK", "RBRACK", "IDENT", "LPAREN", "RPAREN", "COLON", "LCURLY", "RCURLY", "GOES", "ATCHAR", "COMMA", "EQUALS", "DOT", "STAR", "FOLLOWED_BY", "LT", "GT", "QUESTION", "BOR", "PLUS", "STRING_LITERAL", "QUOTED_STRING_LITERAL", "BAND", "BXOR", "SQL_NE", "NOT_EQUAL", "LE", "GE", "LOR", "MINUS", "DIV", "MOD", "NUM_INT", "FOLLOWMAX_BEGIN", "FOLLOWMAX_END", "ESCAPECHAR", "TICKED_STRING_LITERAL", "NUM_LONG", "NUM_FLOAT", "EQUAL", "LNOT", "BNOT", "DIV_ASSIGN", "PLUS_ASSIGN", "INC", "MINUS_ASSIGN", "DEC", "STAR_ASSIGN", "MOD_ASSIGN", "BXOR_ASSIGN", "BOR_ASSIGN", "BAND_ASSIGN", "LAND", "SEMI", "WS", "SL_COMMENT", "ML_COMMENT", "EscapeSequence", "UnicodeEscape", "OctalEscape", "HexDigit", "EXPONENT", "FLOAT_SUFFIX", "GOPCFGEXP", "EVAL_IS_GROUP_EXPR", "EVAL_ISNOT_GROUP_EXPR"
    };
    public static final int FLOAT_SUFFIX=385;
    public static final int GOPCFGITM=297;
    public static final int NUMERIC_PARAM_LIST=131;
    public static final int OUTERJOIN_EXPR=175;
    public static final int CREATE_COL_TYPE_LIST=256;
    public static final int MERGE_INS=275;
    public static final int TIMEPERIOD_MILLISECONDS=105;
    public static final int CREATE_CTX_FIXED=281;
    public static final int RPAREN=327;
    public static final int LNOT=363;
    public static final int INC=367;
    public static final int CREATE=4;
    public static final int STRING_LITERAL=343;
    public static final int STREAM_EXPR=174;
    public static final int MATCHES=116;
    public static final int METADATASQL=68;
    public static final int EVENT_FILTER_PROPERTY_EXPR=140;
    public static final int GOES=331;
    public static final int REGEXP=9;
    public static final int MATCHED=122;
    public static final int INITIATED=127;
    public static final int FOLLOWED_BY_EXPR=134;
    public static final int RBRACK=324;
    public static final int MATCH_UNTIL_RANGE_CLOSED=254;
    public static final int GE=350;
    public static final int ASC=57;
    public static final int IN_SET=6;
    public static final int EVENT_FILTER_EXPR=139;
    public static final int EVENT_FILTER_NOT_IN=150;
    public static final int NUM_DOUBLE=307;
    public static final int TIMEPERIOD_MILLISEC=103;
    public static final int RETAINUNION=64;
    public static final int DBWHERE_CLAUSE=221;
    public static final int MEDIAN=23;
    public static final int GROUP=44;
    public static final int SUBSELECT_GROUP_EXPR=227;
    public static final int YEAR_PART=207;
    public static final int TYPEOF=78;
    public static final int ESCAPECHAR=358;
    public static final int EXPRCOL=198;
    public static final int SL_COMMENT=378;
    public static final int NULL_TYPE=306;
    public static final int MATCH_UNTIL_RANGE_HALFOPEN=252;
    public static final int GT=339;
    public static final int LAND=375;
    public static final int EVENT_PROP_EXPR=183;
    public static final int LBRACK=323;
    public static final int VIEW_EXPR=156;
    public static final int MERGE_UPD=274;
    public static final int CREATE_SCHEMA_DEF=270;
    public static final int EVENT_FILTER_PROPERTY_EXPR_ATOM=141;
    public static final int ON_MERGE_EXPR=244;
    public static final int TIMEPERIOD_SEC=100;
    public static final int ON_SELECT_EXPR=242;
    public static final int TICKED_STRING_LITERAL=359;
    public static final int SUM=18;
    public static final int JSON_ARRAY=321;
    public static final int HexDigit=383;
    public static final int AT=86;
    public static final int AS=17;
    public static final int TIMEPERIOD_MONTH=90;
    public static final int LEFT=38;
    public static final int AVG=19;
    public static final int PREVIOUS=69;
    public static final int PREVIOUSWINDOW=72;
    public static final int PARTITIONITEM=299;
    public static final int DATABASE_JOIN_EXPR=158;
    public static final int IDENT=325;
    public static final int PLUS=342;
    public static final int EVENT_PROP_INDEXED=186;
    public static final int CREATE_SCHEMA_EXPR=268;
    public static final int CREATE_INDEX_EXPR=236;
    public static final int ACCESS_AGG=265;
    public static final int LIKE=8;
    public static final int OUTER=35;
    public static final int RIGHT_OUTERJOIN_EXPR=178;
    public static final int BY=43;
    public static final int MATCHREC_DEFINE=316;
    public static final int MERGE=121;
    public static final int MERGE_UNM=272;
    public static final int FOLLOWMAX_END=357;
    public static final int LEFT_OUTERJOIN_EXPR=177;
    public static final int GROUP_BY_EXPR=180;
    public static final int EPL_EXPR=308;
    public static final int RIGHT=39;
    public static final int HAVING=45;
    public static final int GOPOUTITM=294;
    public static final int MINUS=352;
    public static final int SEMI=376;
    public static final int INDEXCOL=199;
    public static final int STAR_ASSIGN=370;
    public static final int FIRST_AGGREG=262;
    public static final int COLON=328;
    public static final int PREVIOUSTAIL=70;
    public static final int NOT_IN_SET=215;
    public static final int VALUE_NULL=108;
    public static final int EVENT_PROP_DYNAMIC_SIMPLE=187;
    public static final int NOT_IN_SUBSELECT_EXPR=230;
    public static final int GUARD_EXPR=154;
    public static final int RCURLY=330;
    public static final int EXISTS_SUBSELECT_EXPR=228;
    public static final int WEEK_PART=209;
    public static final int ROW_LIMIT_EXPR=109;
    public static final int SELECTION_EXPR=171;
    public static final int EVAL_IS_GROUP_EXPR=387;
    public static final int LW=76;
    public static final int LT=338;
    public static final int CREATE_CTX=280;
    public static final int ORDER_BY_EXPR=181;
    public static final int NEW_ITEM=277;
    public static final int MOD_ASSIGN=371;
    public static final int IN_SUBSELECT_QUERY_EXPR=231;
    public static final int JSON_FIELD=322;
    public static final int EQUALS=334;
    public static final int COUNT=26;
    public static final int RETAININTERSECTION=65;
    public static final int TERMINATED=128;
    public static final int TIMEPERIOD_WEEKS=93;
    public static final int PATTERN=66;
    public static final int MATCHREC_AFTER_SKIP=314;
    public static final int ESCAPE=10;
    public static final int EVAL_NOTEQUALS_GROUP_EXPR=169;
    public static final int SELECT=27;
    public static final int INTO=55;
    public static final int EVAL_ISNOT_EXPR=167;
    public static final int FLOAT_TYPE=302;
    public static final int COALESCE=22;
    public static final int EVENT_FILTER_BETWEEN=151;
    public static final int ANNOTATION_VALUE=261;
    public static final int CLASS_IDENT=153;
    public static final int MATCHREC_PATTERN_ALTER=312;
    public static final int CREATE_WINDOW_EXPR=237;
    public static final int PROPERTY_SELECTION_STREAM=143;
    public static final int ON_DELETE_EXPR=241;
    public static final int ON=41;
    public static final int DELETE=81;
    public static final int INT_TYPE=300;
    public static final int EVERY_EXPR=14;
    public static final int EVAL_BITWISE_EXPR=161;
    public static final int TIMEPERIOD_HOURS=97;
    public static final int STRING_TYPE=304;
    public static final int MATCHREC_DEFINE_ITEM=317;
    public static final int STDDEV=24;
    public static final int OUTPUT=50;
    public static final int WEEKDAY_OPERATOR=233;
    public static final int DEC=369;
    public static final int WHERE=16;
    public static final int GOPOUT=293;
    public static final int BXOR_ASSIGN=372;
    public static final int AFTER_LIMIT_EXPR=192;
    public static final int SNAPSHOT=82;
    public static final int MAX=20;
    public static final int DEFINE=114;
    public static final int TIMEPERIOD_YEARS=89;
    public static final int TIMEPERIOD_DAYS=95;
    public static final int CONTEXT=126;
    public static final int CREATE_CTX_CAT=284;
    public static final int EVENT_PROP_DYNAMIC_INDEXED=188;
    public static final int BOR_ASSIGN=373;
    public static final int COMMA=333;
    public static final int WHEN_LIMIT_EXPR=195;
    public static final int IS=42;
    public static final int PARTITION=115;
    public static final int SOME=49;
    public static final int EQUAL=362;
    public static final int MATCHREC_MEASURE_ITEM=319;
    public static final int EVENT_FILTER_NOT_BETWEEN=152;
    public static final int IN_RANGE=224;
    public static final int TIMEPERIOD_WEEK=92;
    public static final int PROPERTY_WILDCARD_SELECT=144;
    public static final int INSERTINTO_EXPR=197;
    public static final int UNIDIRECTIONAL=63;
    public static final int MATCH_UNTIL_RANGE_BOUNDED=255;
    public static final int TIMEPERIOD_MINUTES=99;
    public static final int RSTREAM=59;
    public static final int NOT_BETWEEN=216;
    public static final int TIMEPERIOD_MINUTE=98;
    public static final int EVAL_OR_EXPR=163;
    public static final int BAND=345;
    public static final int MATCHREC_PATTERN_ATOM=310;
    public static final int QUOTED_STRING_LITERAL=344;
    public static final int NOT_EXPR=13;
    public static final int QUESTION=340;
    public static final int EVENT_FILTER_IDENT=145;
    public static final int UnicodeEscape=381;
    public static final int DBSELECT_EXPR=219;
    public static final int FOLLOWMAX_BEGIN=356;
    public static final int WINDOW=5;
    public static final int ON_SET_EXPR_ITEM=267;
    public static final int DBFROM_CLAUSE=220;
    public static final int LE=349;
    public static final int EVAL_IDENT=170;
    public static final int CRONTAB_LIMIT_EXPR=193;
    public static final int STAR=336;
    public static final int DOT_EXPR=203;
    public static final int ISTREAM=60;
    public static final int MOD=354;
    public static final int LIB_FUNC_CHAIN=202;
    public static final int EVAL_ISNOT_GROUP_EXPR=388;
    public static final int MONTH_PART=208;
    public static final int EOF=-1;
    public static final int LIB_FUNCTION=201;
    public static final int FULL_OUTERJOIN_EXPR=179;
    public static final int CREATE_CTX_NESTED=287;
    public static final int MATCHREC_PATTERN_CONCAT=311;
    public static final int USING=120;
    public static final int CAST_EXPR=235;
    public static final int TIMEPERIOD_SECONDS=102;
    public static final int NOT_EQUAL=348;
    public static final int LAST_AGGREG=263;
    public static final int NEWKW=124;
    public static final int HOUR_PART=211;
    public static final int FOLLOWED_BY=337;
    public static final int MATCHREC_PATTERN_NESTED=313;
    public static final int GOPPARAMITM=292;
    public static final int METHOD_JOIN_EXPR=250;
    public static final int CREATE_CTX_PART=282;
    public static final int PATTERN_EVERY_DISTINCT_EXPR=138;
    public static final int CREATE_CTX_COAL=283;
    public static final int ELSE=30;
    public static final int MINUS_ASSIGN=368;
    public static final int INSERTINTO_STREAM_NAME=223;
    public static final int UNARY_MINUS=204;
    public static final int LCURLY=329;
    public static final int EVENTS=51;
    public static final int AND_EXPR=12;
    public static final int EVENT_FILTER_NOT_RANGE=148;
    public static final int WS=377;
    public static final int FOLLOWED_BY_ITEM=135;
    public static final int ON_SELECT_INSERT_EXPR=245;
    public static final int GOPPARAM=291;
    public static final int BNOT=364;
    public static final int EVAL_IS_EXPR=166;
    public static final int WHERE_EXPR=159;
    public static final int END=33;
    public static final int INNERJOIN_EXPR=176;
    public static final int TERM_LIMIT_EXPR=196;
    public static final int NOT_REGEXP=218;
    public static final int MATCH_UNTIL_EXPR=251;
    public static final int ANNOTATION=259;
    public static final int LONG_TYPE=301;
    public static final int MATCHREC_PATTERN=309;
    public static final int ATCHAR=332;
    public static final int MINUTE_PART=212;
    public static final int PATTERN_NOT_EXPR=137;
    public static final int SQL_NE=347;
    public static final int UPDATE_EXPR=266;
    public static final int LPAREN=326;
    public static final int IN_SUBSELECT_EXPR=229;
    public static final int BOOLEAN_TRUE=106;
    public static final int OR_EXPR=11;
    public static final int JSON_OBJECT=320;
    public static final int THEN=32;
    public static final int NOT_IN_RANGE=225;
    public static final int MATCHREC_INTERVAL=315;
    public static final int OFFSET=110;
    public static final int SECOND_PART=213;
    public static final int MATCH_RECOGNIZE=112;
    public static final int CASE2=29;
    public static final int BXOR=346;
    public static final int TIMEPERIOD_DAY=94;
    public static final int MERGE_MAT=273;
    public static final int EXISTS=74;
    public static final int TIMEPERIOD_MILLISECOND=104;
    public static final int EVAL_NOTEQUALS_EXPR=165;
    public static final int CREATE_CTX_CATITEM=286;
    public static final int CREATE_VARIABLE_EXPR=249;
    public static final int MATCH_UNTIL_RANGE_HALFCLOSED=253;
    public static final int PATTERN_FILTER_EXPR=136;
    public static final int LAST_OPERATOR=232;
    public static final int NUMBERSETSTAR=258;
    public static final int EVAL_AND_EXPR=162;
    public static final int SET=83;
    public static final int INSTANCEOF=77;
    public static final int EVENT_PROP_SIMPLE=184;
    public static final int MIN=21;
    public static final int PREVIOUSCOUNT=71;
    public static final int VARIANT_LIST=271;
    public static final int EVAL_EQUALS_GROUP_EXPR=168;
    public static final int SCHEMA=62;
    public static final int BAND_ASSIGN=374;
    public static final int CRONTAB_LIMIT_EXPR_PARAM=194;
    public static final int WHEN=31;
    public static final int PLUS_ASSIGN=366;
    public static final int DAY_PART=210;
    public static final int START=125;
    public static final int EVENT_FILTER_IN=149;
    public static final int DIV=353;
    public static final int GOPCFGEXP=386;
    public static final int OBJECT_PARAM_ORDERED_EXPR=133;
    public static final int EXPRESSIONDECL=123;
    public static final int OctalEscape=382;
    public static final int BETWEEN=7;
    public static final int MILLISECOND_PART=214;
    public static final int FIRST=52;
    public static final int PRIOR=73;
    public static final int CAST=79;
    public static final int LOR=351;
    public static final int WILDCARD_SELECT=222;
    public static final int EXPONENT=384;
    public static final int PATTERN_INCL_EXPR=157;
    public static final int WHILE=119;
    public static final int BOOL_TYPE=305;
    public static final int GOPCFG=296;
    public static final int ANNOTATION_ARRAY=260;
    public static final int CASE=28;
    public static final int CREATE_EXPR=279;
    public static final int GOP=290;
    public static final int WINDOW_AGGREG=264;
    public static final int DIV_ASSIGN=365;
    public static final int CREATE_CTX_INIT=285;
    public static final int SQL=67;
    public static final int FULL=40;
    public static final int WEEKDAY=75;
    public static final int INSERT=54;
    public static final int ON_UPDATE_EXPR=243;
    public static final int ARRAY_EXPR=206;
    public static final int CREATE_COL_TYPE=257;
    public static final int LAST=53;
    public static final int BOOLEAN_FALSE=107;
    public static final int TIMEPERIOD_SECOND=101;
    public static final int SUBSELECT_EXPR=226;
    public static final int NUMERIC_PARAM_RANGE=130;
    public static final int CONCAT=200;
    public static final int ON_EXPR=239;
    public static final int NUM_LONG=360;
    public static final int TIME_PERIOD=205;
    public static final int DOUBLE_TYPE=303;
    public static final int ORDER_ELEMENT_EXPR=182;
    public static final int VARIABLE=84;
    public static final int SUBSTITUTION=234;
    public static final int UNTIL=85;
    public static final int ON_SET_EXPR=248;
    public static final int NUM_INT=355;
    public static final int ON_EXPR_FROM=247;
    public static final int NUM_FLOAT=361;
    public static final int FROM=34;
    public static final int DISTINCT=46;
    public static final int EscapeSequence=380;
    public static final int PROPERTY_SELECTION_ELEMENT_EXPR=142;
    public static final int INNER=36;
    public static final int NUMERIC_PARAM_FREQUENCY=132;
    public static final int ORDER=56;
    public static final int EVENT_FILTER_PARAM=146;
    public static final int EVENT_PROP_DYNAMIC_MAPPED=189;
    public static final int IRSTREAM=61;
    public static final int UPDATE=111;
    public static final int FOR=118;
    public static final int ON_STREAM=240;
    public static final int EVENT_FILTER_RANGE=147;
    public static final int INDEX=87;
    public static final int ML_COMMENT=379;
    public static final int TIMEPERIOD_LIMIT_EXPR=191;
    public static final int TIMEPERIOD_HOUR=96;
    public static final int ALL=47;
    public static final int BOR=341;
    public static final int DOT=335;
    public static final int CURRENT_TIMESTAMP=80;
    public static final int MATCHREC_MEASURES=318;
    public static final int EVERY_DISTINCT_EXPR=15;
    public static final int HAVING_EXPR=160;
    public static final int MERGE_DEL=276;
    public static final int EVAL_EQUALS_EXPR=164;
    public static final int NOT_LIKE=217;
    public static final int EVENT_LIMIT_EXPR=190;
    public static final int ON_SELECT_INSERT_OUTPUT=246;
    public static final int CREATE_DATAFLOW=289;
    public static final int AFTER=117;
    public static final int MEASURES=113;
    public static final int AGG_FILTER_EXPR=278;
    public static final int CREATE_CTX_PATTERN=288;
    public static final int JOIN=37;
    public static final int GOPOUTTYP=295;
    public static final int ANY=48;
    public static final int OBSERVER_EXPR=155;
    public static final int CREATE_SCHEMA_EXPR_QUAL=269;
    public static final int EVENT_PROP_MAPPED=185;
    public static final int TIMEPERIOD_YEAR=88;
    public static final int AVEDEV=25;
    public static final int GOPCFGEPL=298;
    public static final int TIMEPERIOD_MONTHS=91;
    public static final int SELECTION_ELEMENT_EXPR=172;
    public static final int CREATE_WINDOW_SELECT_EXPR=238;
    public static final int DESC=58;
    public static final int DATAFLOW=129;
    public static final int SELECTION_STREAM=173;

    // delegates
    // delegators


        public EsperEPL2Ast(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public EsperEPL2Ast(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return EsperEPL2Ast.tokenNames; }
    public String getGrammarFileName() { return "EsperEPL2Ast.g"; }


      private static Log log = LogFactory.getLog(EsperEPL2Ast.class);

      // For pattern processing within EPL
      protected void endPattern() {};

      protected void pushStmtContext() {};
      protected void leaveNode(Tree node) {};
      protected void end() {};

      protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);  
      }

      public void recoverFromMismatchedToken(IntStream intStream, RecognitionException recognitionException, int i, BitSet bitSet) throws RecognitionException {
        throw recognitionException;
      }

      public Object recoverFromMismatchedSet(IntStream intStream, RecognitionException recognitionException, BitSet bitSet) throws RecognitionException {
        throw recognitionException;
      }

      protected boolean recoverFromMismatchedElement(IntStream intStream, RecognitionException recognitionException, BitSet bitSet) {
        throw new RuntimeException("Error recovering from mismatched element", recognitionException);
      }
      
      public void recover(org.antlr.runtime.IntStream intStream, org.antlr.runtime.RecognitionException recognitionException) {
        throw new RuntimeException("Error recovering from recognition exception", recognitionException);
      }



    // $ANTLR start "annotation"
    // EsperEPL2Ast.g:57:1: annotation[boolean isLeaveNode] : ^(a= ANNOTATION CLASS_IDENT ( elementValuePair )* ( elementValue )? ) ;
    public final void annotation(boolean isLeaveNode) throws RecognitionException {
        CommonTree a=null;

        try {
            // EsperEPL2Ast.g:58:2: ( ^(a= ANNOTATION CLASS_IDENT ( elementValuePair )* ( elementValue )? ) )
            // EsperEPL2Ast.g:58:4: ^(a= ANNOTATION CLASS_IDENT ( elementValuePair )* ( elementValue )? )
            {
            a=(CommonTree)match(input,ANNOTATION,FOLLOW_ANNOTATION_in_annotation92); 

            match(input, Token.DOWN, null); 
            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_annotation94); 
            // EsperEPL2Ast.g:58:31: ( elementValuePair )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ANNOTATION_VALUE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // EsperEPL2Ast.g:58:31: elementValuePair
            	    {
            	    pushFollow(FOLLOW_elementValuePair_in_annotation96);
            	    elementValuePair();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // EsperEPL2Ast.g:58:49: ( elementValue )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==CLASS_IDENT||(LA2_0>=ANNOTATION && LA2_0<=ANNOTATION_ARRAY)||(LA2_0>=INT_TYPE && LA2_0<=NULL_TYPE)) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // EsperEPL2Ast.g:58:49: elementValue
                    {
                    pushFollow(FOLLOW_elementValue_in_annotation99);
                    elementValue();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 
             if (isLeaveNode) leaveNode(a); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "annotation"


    // $ANTLR start "elementValuePair"
    // EsperEPL2Ast.g:61:1: elementValuePair : ^(a= ANNOTATION_VALUE IDENT elementValue ) ;
    public final void elementValuePair() throws RecognitionException {
        CommonTree a=null;

        try {
            // EsperEPL2Ast.g:62:2: ( ^(a= ANNOTATION_VALUE IDENT elementValue ) )
            // EsperEPL2Ast.g:62:4: ^(a= ANNOTATION_VALUE IDENT elementValue )
            {
            a=(CommonTree)match(input,ANNOTATION_VALUE,FOLLOW_ANNOTATION_VALUE_in_elementValuePair117); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_elementValuePair119); 
            pushFollow(FOLLOW_elementValue_in_elementValuePair121);
            elementValue();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "elementValuePair"


    // $ANTLR start "elementValue"
    // EsperEPL2Ast.g:65:1: elementValue : ( annotation[false] | ^( ANNOTATION_ARRAY ( elementValue )* ) | constant[false] | CLASS_IDENT );
    public final void elementValue() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:66:6: ( annotation[false] | ^( ANNOTATION_ARRAY ( elementValue )* ) | constant[false] | CLASS_IDENT )
            int alt4=4;
            switch ( input.LA(1) ) {
            case ANNOTATION:
                {
                alt4=1;
                }
                break;
            case ANNOTATION_ARRAY:
                {
                alt4=2;
                }
                break;
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt4=3;
                }
                break;
            case CLASS_IDENT:
                {
                alt4=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // EsperEPL2Ast.g:66:11: annotation[false]
                    {
                    pushFollow(FOLLOW_annotation_in_elementValue148);
                    annotation(false);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:67:5: ^( ANNOTATION_ARRAY ( elementValue )* )
                    {
                    match(input,ANNOTATION_ARRAY,FOLLOW_ANNOTATION_ARRAY_in_elementValue156); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:67:24: ( elementValue )*
                        loop3:
                        do {
                            int alt3=2;
                            int LA3_0 = input.LA(1);

                            if ( (LA3_0==CLASS_IDENT||(LA3_0>=ANNOTATION && LA3_0<=ANNOTATION_ARRAY)||(LA3_0>=INT_TYPE && LA3_0<=NULL_TYPE)) ) {
                                alt3=1;
                            }


                            switch (alt3) {
                        	case 1 :
                        	    // EsperEPL2Ast.g:67:24: elementValue
                        	    {
                        	    pushFollow(FOLLOW_elementValue_in_elementValue158);
                        	    elementValue();

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop3;
                            }
                        } while (true);


                        match(input, Token.UP, null); 
                    }

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:68:8: constant[false]
                    {
                    pushFollow(FOLLOW_constant_in_elementValue169);
                    constant(false);

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:69:8: CLASS_IDENT
                    {
                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_elementValue179); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "elementValue"


    // $ANTLR start "expressionDecl"
    // EsperEPL2Ast.g:75:1: expressionDecl[boolean isLeaveNode] : ^(e= EXPRESSIONDECL IDENT expressionDef ( exprCol )? ( CLASS_IDENT )? ( ^( COLON IDENT ) )? ( LBRACK )? ) ;
    public final void expressionDecl(boolean isLeaveNode) throws RecognitionException {
        CommonTree e=null;

        try {
            // EsperEPL2Ast.g:76:2: ( ^(e= EXPRESSIONDECL IDENT expressionDef ( exprCol )? ( CLASS_IDENT )? ( ^( COLON IDENT ) )? ( LBRACK )? ) )
            // EsperEPL2Ast.g:76:4: ^(e= EXPRESSIONDECL IDENT expressionDef ( exprCol )? ( CLASS_IDENT )? ( ^( COLON IDENT ) )? ( LBRACK )? )
            {
            e=(CommonTree)match(input,EXPRESSIONDECL,FOLLOW_EXPRESSIONDECL_in_expressionDecl205); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_expressionDecl207); 
            pushFollow(FOLLOW_expressionDef_in_expressionDecl209);
            expressionDef();

            state._fsp--;

            // EsperEPL2Ast.g:76:43: ( exprCol )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==EXPRCOL) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // EsperEPL2Ast.g:76:43: exprCol
                    {
                    pushFollow(FOLLOW_exprCol_in_expressionDecl211);
                    exprCol();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:76:52: ( CLASS_IDENT )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==CLASS_IDENT) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // EsperEPL2Ast.g:76:52: CLASS_IDENT
                    {
                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_expressionDecl214); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:76:65: ( ^( COLON IDENT ) )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==COLON) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // EsperEPL2Ast.g:76:66: ^( COLON IDENT )
                    {
                    match(input,COLON,FOLLOW_COLON_in_expressionDecl219); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_expressionDecl221); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:76:83: ( LBRACK )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==LBRACK) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // EsperEPL2Ast.g:76:83: LBRACK
                    {
                    match(input,LBRACK,FOLLOW_LBRACK_in_expressionDecl226); 

                    }
                    break;

            }


            match(input, Token.UP, null); 
             if (isLeaveNode) leaveNode(e); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "expressionDecl"


    // $ANTLR start "expressionDef"
    // EsperEPL2Ast.g:79:1: expressionDef : ( ^( GOES valueExpr ( expressionLambdaDecl )? ) | ^( EXPRESSIONDECL constant[false] ) );
    public final void expressionDef() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:80:2: ( ^( GOES valueExpr ( expressionLambdaDecl )? ) | ^( EXPRESSIONDECL constant[false] ) )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==GOES) ) {
                alt10=1;
            }
            else if ( (LA10_0==EXPRESSIONDECL) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // EsperEPL2Ast.g:80:4: ^( GOES valueExpr ( expressionLambdaDecl )? )
                    {
                    match(input,GOES,FOLLOW_GOES_in_expressionDef243); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_expressionDef245);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:80:21: ( expressionLambdaDecl )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==GOES) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // EsperEPL2Ast.g:80:21: expressionLambdaDecl
                            {
                            pushFollow(FOLLOW_expressionLambdaDecl_in_expressionDef247);
                            expressionLambdaDecl();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:81:4: ^( EXPRESSIONDECL constant[false] )
                    {
                    match(input,EXPRESSIONDECL,FOLLOW_EXPRESSIONDECL_in_expressionDef255); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_constant_in_expressionDef257);
                    constant(false);

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "expressionDef"


    // $ANTLR start "expressionLambdaDecl"
    // EsperEPL2Ast.g:84:1: expressionLambdaDecl : ^( GOES ( IDENT | exprCol ) ) ;
    public final void expressionLambdaDecl() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:85:2: ( ^( GOES ( IDENT | exprCol ) ) )
            // EsperEPL2Ast.g:85:4: ^( GOES ( IDENT | exprCol ) )
            {
            match(input,GOES,FOLLOW_GOES_in_expressionLambdaDecl271); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:85:11: ( IDENT | exprCol )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==IDENT) ) {
                alt11=1;
            }
            else if ( (LA11_0==EXPRCOL) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // EsperEPL2Ast.g:85:12: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_expressionLambdaDecl274); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:85:20: exprCol
                    {
                    pushFollow(FOLLOW_exprCol_in_expressionLambdaDecl278);
                    exprCol();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "expressionLambdaDecl"


    // $ANTLR start "startEPLExpressionRule"
    // EsperEPL2Ast.g:91:1: startEPLExpressionRule : ^( EPL_EXPR ( annotation[true] | expressionDecl[true] )* eplExpressionRule ) ;
    public final void startEPLExpressionRule() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:92:2: ( ^( EPL_EXPR ( annotation[true] | expressionDecl[true] )* eplExpressionRule ) )
            // EsperEPL2Ast.g:92:4: ^( EPL_EXPR ( annotation[true] | expressionDecl[true] )* eplExpressionRule )
            {
            match(input,EPL_EXPR,FOLLOW_EPL_EXPR_in_startEPLExpressionRule295); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:92:15: ( annotation[true] | expressionDecl[true] )*
            loop12:
            do {
                int alt12=3;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==ANNOTATION) ) {
                    alt12=1;
                }
                else if ( (LA12_0==EXPRESSIONDECL) ) {
                    alt12=2;
                }


                switch (alt12) {
            	case 1 :
            	    // EsperEPL2Ast.g:92:16: annotation[true]
            	    {
            	    pushFollow(FOLLOW_annotation_in_startEPLExpressionRule298);
            	    annotation(true);

            	    state._fsp--;


            	    }
            	    break;
            	case 2 :
            	    // EsperEPL2Ast.g:92:35: expressionDecl[true]
            	    {
            	    pushFollow(FOLLOW_expressionDecl_in_startEPLExpressionRule303);
            	    expressionDecl(true);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            pushFollow(FOLLOW_eplExpressionRule_in_startEPLExpressionRule308);
            eplExpressionRule();

            state._fsp--;


            match(input, Token.UP, null); 
             end(); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "startEPLExpressionRule"


    // $ANTLR start "eplExpressionRule"
    // EsperEPL2Ast.g:95:1: eplExpressionRule : ( ( ( contextExpr )? ( selectExpr | createWindowExpr | createIndexExpr | createVariableExpr | createSchemaExpr[true] | onExpr | updateExpr | createDataflow | fafDelete | fafUpdate ) ( forExpr )? ) | createContextExpr | createExpr );
    public final void eplExpressionRule() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:96:2: ( ( ( contextExpr )? ( selectExpr | createWindowExpr | createIndexExpr | createVariableExpr | createSchemaExpr[true] | onExpr | updateExpr | createDataflow | fafDelete | fafUpdate ) ( forExpr )? ) | createContextExpr | createExpr )
            int alt16=3;
            switch ( input.LA(1) ) {
            case DELETE:
            case UPDATE:
            case CONTEXT:
            case SELECTION_EXPR:
            case INSERTINTO_EXPR:
            case CREATE_INDEX_EXPR:
            case CREATE_WINDOW_EXPR:
            case ON_EXPR:
            case CREATE_VARIABLE_EXPR:
            case UPDATE_EXPR:
            case CREATE_SCHEMA_EXPR:
            case CREATE_DATAFLOW:
                {
                alt16=1;
                }
                break;
            case CREATE_CTX:
                {
                alt16=2;
                }
                break;
            case CREATE_EXPR:
                {
                alt16=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // EsperEPL2Ast.g:96:4: ( ( contextExpr )? ( selectExpr | createWindowExpr | createIndexExpr | createVariableExpr | createSchemaExpr[true] | onExpr | updateExpr | createDataflow | fafDelete | fafUpdate ) ( forExpr )? )
                    {
                    // EsperEPL2Ast.g:96:4: ( ( contextExpr )? ( selectExpr | createWindowExpr | createIndexExpr | createVariableExpr | createSchemaExpr[true] | onExpr | updateExpr | createDataflow | fafDelete | fafUpdate ) ( forExpr )? )
                    // EsperEPL2Ast.g:96:5: ( contextExpr )? ( selectExpr | createWindowExpr | createIndexExpr | createVariableExpr | createSchemaExpr[true] | onExpr | updateExpr | createDataflow | fafDelete | fafUpdate ) ( forExpr )?
                    {
                    // EsperEPL2Ast.g:96:5: ( contextExpr )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==CONTEXT) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // EsperEPL2Ast.g:96:5: contextExpr
                            {
                            pushFollow(FOLLOW_contextExpr_in_eplExpressionRule325);
                            contextExpr();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:96:18: ( selectExpr | createWindowExpr | createIndexExpr | createVariableExpr | createSchemaExpr[true] | onExpr | updateExpr | createDataflow | fafDelete | fafUpdate )
                    int alt14=10;
                    switch ( input.LA(1) ) {
                    case SELECTION_EXPR:
                    case INSERTINTO_EXPR:
                        {
                        alt14=1;
                        }
                        break;
                    case CREATE_WINDOW_EXPR:
                        {
                        alt14=2;
                        }
                        break;
                    case CREATE_INDEX_EXPR:
                        {
                        alt14=3;
                        }
                        break;
                    case CREATE_VARIABLE_EXPR:
                        {
                        alt14=4;
                        }
                        break;
                    case CREATE_SCHEMA_EXPR:
                        {
                        alt14=5;
                        }
                        break;
                    case ON_EXPR:
                        {
                        alt14=6;
                        }
                        break;
                    case UPDATE_EXPR:
                        {
                        alt14=7;
                        }
                        break;
                    case CREATE_DATAFLOW:
                        {
                        alt14=8;
                        }
                        break;
                    case DELETE:
                        {
                        alt14=9;
                        }
                        break;
                    case UPDATE:
                        {
                        alt14=10;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 0, input);

                        throw nvae;
                    }

                    switch (alt14) {
                        case 1 :
                            // EsperEPL2Ast.g:96:19: selectExpr
                            {
                            pushFollow(FOLLOW_selectExpr_in_eplExpressionRule329);
                            selectExpr();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:96:32: createWindowExpr
                            {
                            pushFollow(FOLLOW_createWindowExpr_in_eplExpressionRule333);
                            createWindowExpr();

                            state._fsp--;


                            }
                            break;
                        case 3 :
                            // EsperEPL2Ast.g:96:51: createIndexExpr
                            {
                            pushFollow(FOLLOW_createIndexExpr_in_eplExpressionRule337);
                            createIndexExpr();

                            state._fsp--;


                            }
                            break;
                        case 4 :
                            // EsperEPL2Ast.g:96:69: createVariableExpr
                            {
                            pushFollow(FOLLOW_createVariableExpr_in_eplExpressionRule341);
                            createVariableExpr();

                            state._fsp--;


                            }
                            break;
                        case 5 :
                            // EsperEPL2Ast.g:96:90: createSchemaExpr[true]
                            {
                            pushFollow(FOLLOW_createSchemaExpr_in_eplExpressionRule345);
                            createSchemaExpr(true);

                            state._fsp--;


                            }
                            break;
                        case 6 :
                            // EsperEPL2Ast.g:96:115: onExpr
                            {
                            pushFollow(FOLLOW_onExpr_in_eplExpressionRule350);
                            onExpr();

                            state._fsp--;


                            }
                            break;
                        case 7 :
                            // EsperEPL2Ast.g:96:124: updateExpr
                            {
                            pushFollow(FOLLOW_updateExpr_in_eplExpressionRule354);
                            updateExpr();

                            state._fsp--;


                            }
                            break;
                        case 8 :
                            // EsperEPL2Ast.g:96:137: createDataflow
                            {
                            pushFollow(FOLLOW_createDataflow_in_eplExpressionRule358);
                            createDataflow();

                            state._fsp--;


                            }
                            break;
                        case 9 :
                            // EsperEPL2Ast.g:96:154: fafDelete
                            {
                            pushFollow(FOLLOW_fafDelete_in_eplExpressionRule362);
                            fafDelete();

                            state._fsp--;


                            }
                            break;
                        case 10 :
                            // EsperEPL2Ast.g:96:166: fafUpdate
                            {
                            pushFollow(FOLLOW_fafUpdate_in_eplExpressionRule366);
                            fafUpdate();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:96:177: ( forExpr )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==FOR) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // EsperEPL2Ast.g:96:177: forExpr
                            {
                            pushFollow(FOLLOW_forExpr_in_eplExpressionRule369);
                            forExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:97:4: createContextExpr
                    {
                    pushFollow(FOLLOW_createContextExpr_in_eplExpressionRule376);
                    createContextExpr();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:97:24: createExpr
                    {
                    pushFollow(FOLLOW_createExpr_in_eplExpressionRule380);
                    createExpr();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "eplExpressionRule"


    // $ANTLR start "contextExpr"
    // EsperEPL2Ast.g:100:1: contextExpr : ^(i= CONTEXT IDENT ) ;
    public final void contextExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:101:2: ( ^(i= CONTEXT IDENT ) )
            // EsperEPL2Ast.g:101:4: ^(i= CONTEXT IDENT )
            {
            i=(CommonTree)match(input,CONTEXT,FOLLOW_CONTEXT_in_contextExpr395); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_contextExpr397); 
             leaveNode(i); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "contextExpr"


    // $ANTLR start "onExpr"
    // EsperEPL2Ast.g:104:1: onExpr : ^(i= ON_EXPR onStreamExpr ( onDeleteExpr | onUpdateExpr | onSelectExpr ( ( onSelectInsertExpr )+ ( onSelectInsertOutput )? )? | onSetExpr | onMergeExpr ) ) ;
    public final void onExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:105:2: ( ^(i= ON_EXPR onStreamExpr ( onDeleteExpr | onUpdateExpr | onSelectExpr ( ( onSelectInsertExpr )+ ( onSelectInsertOutput )? )? | onSetExpr | onMergeExpr ) ) )
            // EsperEPL2Ast.g:105:4: ^(i= ON_EXPR onStreamExpr ( onDeleteExpr | onUpdateExpr | onSelectExpr ( ( onSelectInsertExpr )+ ( onSelectInsertOutput )? )? | onSetExpr | onMergeExpr ) )
            {
            i=(CommonTree)match(input,ON_EXPR,FOLLOW_ON_EXPR_in_onExpr416); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_onStreamExpr_in_onExpr418);
            onStreamExpr();

            state._fsp--;

            // EsperEPL2Ast.g:106:3: ( onDeleteExpr | onUpdateExpr | onSelectExpr ( ( onSelectInsertExpr )+ ( onSelectInsertOutput )? )? | onSetExpr | onMergeExpr )
            int alt20=5;
            switch ( input.LA(1) ) {
            case ON_DELETE_EXPR:
                {
                alt20=1;
                }
                break;
            case ON_UPDATE_EXPR:
                {
                alt20=2;
                }
                break;
            case ON_SELECT_EXPR:
                {
                alt20=3;
                }
                break;
            case ON_SET_EXPR:
                {
                alt20=4;
                }
                break;
            case ON_MERGE_EXPR:
                {
                alt20=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // EsperEPL2Ast.g:106:4: onDeleteExpr
                    {
                    pushFollow(FOLLOW_onDeleteExpr_in_onExpr423);
                    onDeleteExpr();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:106:19: onUpdateExpr
                    {
                    pushFollow(FOLLOW_onUpdateExpr_in_onExpr427);
                    onUpdateExpr();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:106:34: onSelectExpr ( ( onSelectInsertExpr )+ ( onSelectInsertOutput )? )?
                    {
                    pushFollow(FOLLOW_onSelectExpr_in_onExpr431);
                    onSelectExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:106:47: ( ( onSelectInsertExpr )+ ( onSelectInsertOutput )? )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==ON_SELECT_INSERT_EXPR) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // EsperEPL2Ast.g:106:48: ( onSelectInsertExpr )+ ( onSelectInsertOutput )?
                            {
                            // EsperEPL2Ast.g:106:48: ( onSelectInsertExpr )+
                            int cnt17=0;
                            loop17:
                            do {
                                int alt17=2;
                                int LA17_0 = input.LA(1);

                                if ( (LA17_0==ON_SELECT_INSERT_EXPR) ) {
                                    alt17=1;
                                }


                                switch (alt17) {
                            	case 1 :
                            	    // EsperEPL2Ast.g:106:48: onSelectInsertExpr
                            	    {
                            	    pushFollow(FOLLOW_onSelectInsertExpr_in_onExpr434);
                            	    onSelectInsertExpr();

                            	    state._fsp--;


                            	    }
                            	    break;

                            	default :
                            	    if ( cnt17 >= 1 ) break loop17;
                                        EarlyExitException eee =
                                            new EarlyExitException(17, input);
                                        throw eee;
                                }
                                cnt17++;
                            } while (true);

                            // EsperEPL2Ast.g:106:68: ( onSelectInsertOutput )?
                            int alt18=2;
                            int LA18_0 = input.LA(1);

                            if ( (LA18_0==ON_SELECT_INSERT_OUTPUT) ) {
                                alt18=1;
                            }
                            switch (alt18) {
                                case 1 :
                                    // EsperEPL2Ast.g:106:68: onSelectInsertOutput
                                    {
                                    pushFollow(FOLLOW_onSelectInsertOutput_in_onExpr437);
                                    onSelectInsertOutput();

                                    state._fsp--;


                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:106:94: onSetExpr
                    {
                    pushFollow(FOLLOW_onSetExpr_in_onExpr444);
                    onSetExpr();

                    state._fsp--;


                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:106:106: onMergeExpr
                    {
                    pushFollow(FOLLOW_onMergeExpr_in_onExpr448);
                    onMergeExpr();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(i); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onExpr"


    // $ANTLR start "onStreamExpr"
    // EsperEPL2Ast.g:110:1: onStreamExpr : ^(s= ON_STREAM ( eventFilterExpr[true] | patternInclusionExpression ) ( IDENT )? ) ;
    public final void onStreamExpr() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:111:2: ( ^(s= ON_STREAM ( eventFilterExpr[true] | patternInclusionExpression ) ( IDENT )? ) )
            // EsperEPL2Ast.g:111:4: ^(s= ON_STREAM ( eventFilterExpr[true] | patternInclusionExpression ) ( IDENT )? )
            {
            s=(CommonTree)match(input,ON_STREAM,FOLLOW_ON_STREAM_in_onStreamExpr470); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:111:18: ( eventFilterExpr[true] | patternInclusionExpression )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==EVENT_FILTER_EXPR) ) {
                alt21=1;
            }
            else if ( (LA21_0==PATTERN_INCL_EXPR) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // EsperEPL2Ast.g:111:19: eventFilterExpr[true]
                    {
                    pushFollow(FOLLOW_eventFilterExpr_in_onStreamExpr473);
                    eventFilterExpr(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:111:43: patternInclusionExpression
                    {
                    pushFollow(FOLLOW_patternInclusionExpression_in_onStreamExpr478);
                    patternInclusionExpression();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:111:71: ( IDENT )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==IDENT) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // EsperEPL2Ast.g:111:71: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_onStreamExpr481); 

                    }
                    break;

            }

             leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onStreamExpr"


    // $ANTLR start "onMergeExpr"
    // EsperEPL2Ast.g:114:1: onMergeExpr : ^(m= ON_MERGE_EXPR IDENT ( IDENT )? ( mergeItem )+ ( whereClause[true] )? ) ;
    public final void onMergeExpr() throws RecognitionException {
        CommonTree m=null;

        try {
            // EsperEPL2Ast.g:115:2: ( ^(m= ON_MERGE_EXPR IDENT ( IDENT )? ( mergeItem )+ ( whereClause[true] )? ) )
            // EsperEPL2Ast.g:115:4: ^(m= ON_MERGE_EXPR IDENT ( IDENT )? ( mergeItem )+ ( whereClause[true] )? )
            {
            m=(CommonTree)match(input,ON_MERGE_EXPR,FOLLOW_ON_MERGE_EXPR_in_onMergeExpr499); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_onMergeExpr501); 
            // EsperEPL2Ast.g:115:28: ( IDENT )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==IDENT) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // EsperEPL2Ast.g:115:28: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_onMergeExpr503); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:115:35: ( mergeItem )+
            int cnt24=0;
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( ((LA24_0>=MERGE_UNM && LA24_0<=MERGE_MAT)) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // EsperEPL2Ast.g:115:35: mergeItem
            	    {
            	    pushFollow(FOLLOW_mergeItem_in_onMergeExpr506);
            	    mergeItem();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt24 >= 1 ) break loop24;
                        EarlyExitException eee =
                            new EarlyExitException(24, input);
                        throw eee;
                }
                cnt24++;
            } while (true);

            // EsperEPL2Ast.g:115:46: ( whereClause[true] )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==WHERE_EXPR) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // EsperEPL2Ast.g:115:46: whereClause[true]
                    {
                    pushFollow(FOLLOW_whereClause_in_onMergeExpr509);
                    whereClause(true);

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onMergeExpr"


    // $ANTLR start "mergeItem"
    // EsperEPL2Ast.g:118:1: mergeItem : ( mergeMatched | mergeUnmatched ) ;
    public final void mergeItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:119:2: ( ( mergeMatched | mergeUnmatched ) )
            // EsperEPL2Ast.g:119:4: ( mergeMatched | mergeUnmatched )
            {
            // EsperEPL2Ast.g:119:4: ( mergeMatched | mergeUnmatched )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==MERGE_MAT) ) {
                alt26=1;
            }
            else if ( (LA26_0==MERGE_UNM) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // EsperEPL2Ast.g:119:5: mergeMatched
                    {
                    pushFollow(FOLLOW_mergeMatched_in_mergeItem525);
                    mergeMatched();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:119:20: mergeUnmatched
                    {
                    pushFollow(FOLLOW_mergeUnmatched_in_mergeItem529);
                    mergeUnmatched();

                    state._fsp--;


                    }
                    break;

            }


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "mergeItem"


    // $ANTLR start "mergeMatched"
    // EsperEPL2Ast.g:122:1: mergeMatched : ^(m= MERGE_MAT ( mergeMatchedItem )+ ( valueExpr )? ) ;
    public final void mergeMatched() throws RecognitionException {
        CommonTree m=null;

        try {
            // EsperEPL2Ast.g:123:2: ( ^(m= MERGE_MAT ( mergeMatchedItem )+ ( valueExpr )? ) )
            // EsperEPL2Ast.g:123:4: ^(m= MERGE_MAT ( mergeMatchedItem )+ ( valueExpr )? )
            {
            m=(CommonTree)match(input,MERGE_MAT,FOLLOW_MERGE_MAT_in_mergeMatched544); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:123:18: ( mergeMatchedItem )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0>=MERGE_UPD && LA27_0<=MERGE_DEL)) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // EsperEPL2Ast.g:123:18: mergeMatchedItem
            	    {
            	    pushFollow(FOLLOW_mergeMatchedItem_in_mergeMatched546);
            	    mergeMatchedItem();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt27 >= 1 ) break loop27;
                        EarlyExitException eee =
                            new EarlyExitException(27, input);
                        throw eee;
                }
                cnt27++;
            } while (true);

            // EsperEPL2Ast.g:123:36: ( valueExpr )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( ((LA28_0>=IN_SET && LA28_0<=REGEXP)||LA28_0==NOT_EXPR||(LA28_0>=SUM && LA28_0<=AVG)||(LA28_0>=COALESCE && LA28_0<=COUNT)||(LA28_0>=CASE && LA28_0<=CASE2)||LA28_0==ISTREAM||(LA28_0>=PREVIOUS && LA28_0<=EXISTS)||(LA28_0>=INSTANCEOF && LA28_0<=CURRENT_TIMESTAMP)||LA28_0==NEWKW||(LA28_0>=EVAL_AND_EXPR && LA28_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA28_0==EVENT_PROP_EXPR||LA28_0==CONCAT||(LA28_0>=LIB_FUNC_CHAIN && LA28_0<=DOT_EXPR)||LA28_0==ARRAY_EXPR||(LA28_0>=NOT_IN_SET && LA28_0<=NOT_REGEXP)||(LA28_0>=IN_RANGE && LA28_0<=SUBSELECT_EXPR)||(LA28_0>=EXISTS_SUBSELECT_EXPR && LA28_0<=NOT_IN_SUBSELECT_EXPR)||LA28_0==SUBSTITUTION||(LA28_0>=FIRST_AGGREG && LA28_0<=WINDOW_AGGREG)||(LA28_0>=INT_TYPE && LA28_0<=NULL_TYPE)||(LA28_0>=JSON_OBJECT && LA28_0<=JSON_ARRAY)||LA28_0==STAR||(LA28_0>=LT && LA28_0<=GT)||(LA28_0>=BOR && LA28_0<=PLUS)||(LA28_0>=BAND && LA28_0<=BXOR)||(LA28_0>=LE && LA28_0<=GE)||(LA28_0>=MINUS && LA28_0<=MOD)||(LA28_0>=EVAL_IS_GROUP_EXPR && LA28_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // EsperEPL2Ast.g:123:36: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_mergeMatched549);
                    valueExpr();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(m); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "mergeMatched"


    // $ANTLR start "mergeMatchedItem"
    // EsperEPL2Ast.g:126:1: mergeMatchedItem : ( ^(m= MERGE_UPD ( onSetAssignment )* ( whereClause[false] )? ) | ^(d= MERGE_DEL ( whereClause[false] )? INT_TYPE ) | mergeInsert );
    public final void mergeMatchedItem() throws RecognitionException {
        CommonTree m=null;
        CommonTree d=null;

        try {
            // EsperEPL2Ast.g:127:2: ( ^(m= MERGE_UPD ( onSetAssignment )* ( whereClause[false] )? ) | ^(d= MERGE_DEL ( whereClause[false] )? INT_TYPE ) | mergeInsert )
            int alt32=3;
            switch ( input.LA(1) ) {
            case MERGE_UPD:
                {
                alt32=1;
                }
                break;
            case MERGE_DEL:
                {
                alt32=2;
                }
                break;
            case MERGE_INS:
                {
                alt32=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // EsperEPL2Ast.g:127:4: ^(m= MERGE_UPD ( onSetAssignment )* ( whereClause[false] )? )
                    {
                    m=(CommonTree)match(input,MERGE_UPD,FOLLOW_MERGE_UPD_in_mergeMatchedItem567); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:127:18: ( onSetAssignment )*
                        loop29:
                        do {
                            int alt29=2;
                            int LA29_0 = input.LA(1);

                            if ( (LA29_0==ON_SET_EXPR_ITEM) ) {
                                alt29=1;
                            }


                            switch (alt29) {
                        	case 1 :
                        	    // EsperEPL2Ast.g:127:18: onSetAssignment
                        	    {
                        	    pushFollow(FOLLOW_onSetAssignment_in_mergeMatchedItem569);
                        	    onSetAssignment();

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop29;
                            }
                        } while (true);

                        // EsperEPL2Ast.g:127:35: ( whereClause[false] )?
                        int alt30=2;
                        int LA30_0 = input.LA(1);

                        if ( (LA30_0==WHERE_EXPR) ) {
                            alt30=1;
                        }
                        switch (alt30) {
                            case 1 :
                                // EsperEPL2Ast.g:127:35: whereClause[false]
                                {
                                pushFollow(FOLLOW_whereClause_in_mergeMatchedItem572);
                                whereClause(false);

                                state._fsp--;


                                }
                                break;

                        }

                         leaveNode(m); 

                        match(input, Token.UP, null); 
                    }

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:128:4: ^(d= MERGE_DEL ( whereClause[false] )? INT_TYPE )
                    {
                    d=(CommonTree)match(input,MERGE_DEL,FOLLOW_MERGE_DEL_in_mergeMatchedItem585); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:128:18: ( whereClause[false] )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==WHERE_EXPR) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // EsperEPL2Ast.g:128:18: whereClause[false]
                            {
                            pushFollow(FOLLOW_whereClause_in_mergeMatchedItem587);
                            whereClause(false);

                            state._fsp--;


                            }
                            break;

                    }

                    match(input,INT_TYPE,FOLLOW_INT_TYPE_in_mergeMatchedItem591); 
                     leaveNode(d); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:129:4: mergeInsert
                    {
                    pushFollow(FOLLOW_mergeInsert_in_mergeMatchedItem599);
                    mergeInsert();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "mergeMatchedItem"


    // $ANTLR start "mergeUnmatched"
    // EsperEPL2Ast.g:132:1: mergeUnmatched : ^(m= MERGE_UNM ( mergeInsert )+ ( valueExpr )? ) ;
    public final void mergeUnmatched() throws RecognitionException {
        CommonTree m=null;

        try {
            // EsperEPL2Ast.g:133:2: ( ^(m= MERGE_UNM ( mergeInsert )+ ( valueExpr )? ) )
            // EsperEPL2Ast.g:133:4: ^(m= MERGE_UNM ( mergeInsert )+ ( valueExpr )? )
            {
            m=(CommonTree)match(input,MERGE_UNM,FOLLOW_MERGE_UNM_in_mergeUnmatched613); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:133:18: ( mergeInsert )+
            int cnt33=0;
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==MERGE_INS) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // EsperEPL2Ast.g:133:18: mergeInsert
            	    {
            	    pushFollow(FOLLOW_mergeInsert_in_mergeUnmatched615);
            	    mergeInsert();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt33 >= 1 ) break loop33;
                        EarlyExitException eee =
                            new EarlyExitException(33, input);
                        throw eee;
                }
                cnt33++;
            } while (true);

            // EsperEPL2Ast.g:133:31: ( valueExpr )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( ((LA34_0>=IN_SET && LA34_0<=REGEXP)||LA34_0==NOT_EXPR||(LA34_0>=SUM && LA34_0<=AVG)||(LA34_0>=COALESCE && LA34_0<=COUNT)||(LA34_0>=CASE && LA34_0<=CASE2)||LA34_0==ISTREAM||(LA34_0>=PREVIOUS && LA34_0<=EXISTS)||(LA34_0>=INSTANCEOF && LA34_0<=CURRENT_TIMESTAMP)||LA34_0==NEWKW||(LA34_0>=EVAL_AND_EXPR && LA34_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA34_0==EVENT_PROP_EXPR||LA34_0==CONCAT||(LA34_0>=LIB_FUNC_CHAIN && LA34_0<=DOT_EXPR)||LA34_0==ARRAY_EXPR||(LA34_0>=NOT_IN_SET && LA34_0<=NOT_REGEXP)||(LA34_0>=IN_RANGE && LA34_0<=SUBSELECT_EXPR)||(LA34_0>=EXISTS_SUBSELECT_EXPR && LA34_0<=NOT_IN_SUBSELECT_EXPR)||LA34_0==SUBSTITUTION||(LA34_0>=FIRST_AGGREG && LA34_0<=WINDOW_AGGREG)||(LA34_0>=INT_TYPE && LA34_0<=NULL_TYPE)||(LA34_0>=JSON_OBJECT && LA34_0<=JSON_ARRAY)||LA34_0==STAR||(LA34_0>=LT && LA34_0<=GT)||(LA34_0>=BOR && LA34_0<=PLUS)||(LA34_0>=BAND && LA34_0<=BXOR)||(LA34_0>=LE && LA34_0<=GE)||(LA34_0>=MINUS && LA34_0<=MOD)||(LA34_0>=EVAL_IS_GROUP_EXPR && LA34_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // EsperEPL2Ast.g:133:31: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_mergeUnmatched618);
                    valueExpr();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(m); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "mergeUnmatched"


    // $ANTLR start "mergeInsert"
    // EsperEPL2Ast.g:136:1: mergeInsert : ^(um= MERGE_INS selectionList ( CLASS_IDENT )? ( exprCol )? ( whereClause[false] )? ) ;
    public final void mergeInsert() throws RecognitionException {
        CommonTree um=null;

        try {
            // EsperEPL2Ast.g:137:2: ( ^(um= MERGE_INS selectionList ( CLASS_IDENT )? ( exprCol )? ( whereClause[false] )? ) )
            // EsperEPL2Ast.g:137:4: ^(um= MERGE_INS selectionList ( CLASS_IDENT )? ( exprCol )? ( whereClause[false] )? )
            {
            um=(CommonTree)match(input,MERGE_INS,FOLLOW_MERGE_INS_in_mergeInsert637); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_selectionList_in_mergeInsert639);
            selectionList();

            state._fsp--;

            // EsperEPL2Ast.g:137:33: ( CLASS_IDENT )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==CLASS_IDENT) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // EsperEPL2Ast.g:137:33: CLASS_IDENT
                    {
                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_mergeInsert641); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:137:46: ( exprCol )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==EXPRCOL) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // EsperEPL2Ast.g:137:46: exprCol
                    {
                    pushFollow(FOLLOW_exprCol_in_mergeInsert644);
                    exprCol();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:137:55: ( whereClause[false] )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==WHERE_EXPR) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // EsperEPL2Ast.g:137:55: whereClause[false]
                    {
                    pushFollow(FOLLOW_whereClause_in_mergeInsert647);
                    whereClause(false);

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(um); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "mergeInsert"


    // $ANTLR start "updateExpr"
    // EsperEPL2Ast.g:140:1: updateExpr : ^(u= UPDATE_EXPR updateDetails ) ;
    public final void updateExpr() throws RecognitionException {
        CommonTree u=null;

        try {
            // EsperEPL2Ast.g:141:2: ( ^(u= UPDATE_EXPR updateDetails ) )
            // EsperEPL2Ast.g:141:4: ^(u= UPDATE_EXPR updateDetails )
            {
            u=(CommonTree)match(input,UPDATE_EXPR,FOLLOW_UPDATE_EXPR_in_updateExpr667); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_updateDetails_in_updateExpr669);
            updateDetails();

            state._fsp--;

             leaveNode(u); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "updateExpr"


    // $ANTLR start "updateDetails"
    // EsperEPL2Ast.g:144:1: updateDetails : ^(u= UPDATE CLASS_IDENT ( IDENT )? ( onSetAssignment )+ ( whereClause[false] )? ) ;
    public final void updateDetails() throws RecognitionException {
        CommonTree u=null;

        try {
            // EsperEPL2Ast.g:145:2: ( ^(u= UPDATE CLASS_IDENT ( IDENT )? ( onSetAssignment )+ ( whereClause[false] )? ) )
            // EsperEPL2Ast.g:145:4: ^(u= UPDATE CLASS_IDENT ( IDENT )? ( onSetAssignment )+ ( whereClause[false] )? )
            {
            u=(CommonTree)match(input,UPDATE,FOLLOW_UPDATE_in_updateDetails686); 

            match(input, Token.DOWN, null); 
            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_updateDetails688); 
            // EsperEPL2Ast.g:145:27: ( IDENT )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==IDENT) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // EsperEPL2Ast.g:145:27: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_updateDetails690); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:145:34: ( onSetAssignment )+
            int cnt39=0;
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( (LA39_0==ON_SET_EXPR_ITEM) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // EsperEPL2Ast.g:145:34: onSetAssignment
            	    {
            	    pushFollow(FOLLOW_onSetAssignment_in_updateDetails693);
            	    onSetAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt39 >= 1 ) break loop39;
                        EarlyExitException eee =
                            new EarlyExitException(39, input);
                        throw eee;
                }
                cnt39++;
            } while (true);

            // EsperEPL2Ast.g:145:51: ( whereClause[false] )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==WHERE_EXPR) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // EsperEPL2Ast.g:145:51: whereClause[false]
                    {
                    pushFollow(FOLLOW_whereClause_in_updateDetails696);
                    whereClause(false);

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "updateDetails"


    // $ANTLR start "onDeleteExpr"
    // EsperEPL2Ast.g:148:1: onDeleteExpr : ^( ON_DELETE_EXPR onExprFrom ( whereClause[true] )? ) ;
    public final void onDeleteExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:149:2: ( ^( ON_DELETE_EXPR onExprFrom ( whereClause[true] )? ) )
            // EsperEPL2Ast.g:149:4: ^( ON_DELETE_EXPR onExprFrom ( whereClause[true] )? )
            {
            match(input,ON_DELETE_EXPR,FOLLOW_ON_DELETE_EXPR_in_onDeleteExpr711); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_onExprFrom_in_onDeleteExpr713);
            onExprFrom();

            state._fsp--;

            // EsperEPL2Ast.g:149:32: ( whereClause[true] )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==WHERE_EXPR) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // EsperEPL2Ast.g:149:33: whereClause[true]
                    {
                    pushFollow(FOLLOW_whereClause_in_onDeleteExpr716);
                    whereClause(true);

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onDeleteExpr"


    // $ANTLR start "onSelectExpr"
    // EsperEPL2Ast.g:152:1: onSelectExpr : ^(s= ON_SELECT_EXPR ( insertIntoExpr )? ( DELETE )? ( DISTINCT )? selectionList ( onExprFrom )? ( whereClause[true] )? ( groupByClause )? ( havingClause )? ( orderByClause )? ( rowLimitClause )? ) ;
    public final void onSelectExpr() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:153:2: ( ^(s= ON_SELECT_EXPR ( insertIntoExpr )? ( DELETE )? ( DISTINCT )? selectionList ( onExprFrom )? ( whereClause[true] )? ( groupByClause )? ( havingClause )? ( orderByClause )? ( rowLimitClause )? ) )
            // EsperEPL2Ast.g:153:4: ^(s= ON_SELECT_EXPR ( insertIntoExpr )? ( DELETE )? ( DISTINCT )? selectionList ( onExprFrom )? ( whereClause[true] )? ( groupByClause )? ( havingClause )? ( orderByClause )? ( rowLimitClause )? )
            {
            s=(CommonTree)match(input,ON_SELECT_EXPR,FOLLOW_ON_SELECT_EXPR_in_onSelectExpr736); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:153:23: ( insertIntoExpr )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==INSERTINTO_EXPR) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // EsperEPL2Ast.g:153:23: insertIntoExpr
                    {
                    pushFollow(FOLLOW_insertIntoExpr_in_onSelectExpr738);
                    insertIntoExpr();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:153:39: ( DELETE )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==DELETE) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // EsperEPL2Ast.g:153:39: DELETE
                    {
                    match(input,DELETE,FOLLOW_DELETE_in_onSelectExpr741); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:153:47: ( DISTINCT )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==DISTINCT) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // EsperEPL2Ast.g:153:47: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_onSelectExpr744); 

                    }
                    break;

            }

            pushFollow(FOLLOW_selectionList_in_onSelectExpr747);
            selectionList();

            state._fsp--;

            // EsperEPL2Ast.g:153:71: ( onExprFrom )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==ON_EXPR_FROM) ) {
                alt45=1;
            }
            switch (alt45) {
                case 1 :
                    // EsperEPL2Ast.g:153:71: onExprFrom
                    {
                    pushFollow(FOLLOW_onExprFrom_in_onSelectExpr749);
                    onExprFrom();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:153:83: ( whereClause[true] )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==WHERE_EXPR) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // EsperEPL2Ast.g:153:83: whereClause[true]
                    {
                    pushFollow(FOLLOW_whereClause_in_onSelectExpr752);
                    whereClause(true);

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:153:102: ( groupByClause )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==GROUP_BY_EXPR) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // EsperEPL2Ast.g:153:102: groupByClause
                    {
                    pushFollow(FOLLOW_groupByClause_in_onSelectExpr756);
                    groupByClause();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:153:117: ( havingClause )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==HAVING_EXPR) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // EsperEPL2Ast.g:153:117: havingClause
                    {
                    pushFollow(FOLLOW_havingClause_in_onSelectExpr759);
                    havingClause();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:153:131: ( orderByClause )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==ORDER_BY_EXPR) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // EsperEPL2Ast.g:153:131: orderByClause
                    {
                    pushFollow(FOLLOW_orderByClause_in_onSelectExpr762);
                    orderByClause();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:153:146: ( rowLimitClause )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==ROW_LIMIT_EXPR) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // EsperEPL2Ast.g:153:146: rowLimitClause
                    {
                    pushFollow(FOLLOW_rowLimitClause_in_onSelectExpr765);
                    rowLimitClause();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onSelectExpr"


    // $ANTLR start "onSelectInsertExpr"
    // EsperEPL2Ast.g:156:1: onSelectInsertExpr : ^( ON_SELECT_INSERT_EXPR insertIntoExpr selectionList ( whereClause[true] )? ) ;
    public final void onSelectInsertExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:157:2: ( ^( ON_SELECT_INSERT_EXPR insertIntoExpr selectionList ( whereClause[true] )? ) )
            // EsperEPL2Ast.g:157:4: ^( ON_SELECT_INSERT_EXPR insertIntoExpr selectionList ( whereClause[true] )? )
            {
            pushStmtContext();
            match(input,ON_SELECT_INSERT_EXPR,FOLLOW_ON_SELECT_INSERT_EXPR_in_onSelectInsertExpr785); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_insertIntoExpr_in_onSelectInsertExpr787);
            insertIntoExpr();

            state._fsp--;

            pushFollow(FOLLOW_selectionList_in_onSelectInsertExpr789);
            selectionList();

            state._fsp--;

            // EsperEPL2Ast.g:157:78: ( whereClause[true] )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==WHERE_EXPR) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // EsperEPL2Ast.g:157:78: whereClause[true]
                    {
                    pushFollow(FOLLOW_whereClause_in_onSelectInsertExpr791);
                    whereClause(true);

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onSelectInsertExpr"


    // $ANTLR start "onSelectInsertOutput"
    // EsperEPL2Ast.g:160:1: onSelectInsertOutput : ^( ON_SELECT_INSERT_OUTPUT ( ALL | FIRST ) ) ;
    public final void onSelectInsertOutput() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:161:2: ( ^( ON_SELECT_INSERT_OUTPUT ( ALL | FIRST ) ) )
            // EsperEPL2Ast.g:161:4: ^( ON_SELECT_INSERT_OUTPUT ( ALL | FIRST ) )
            {
            match(input,ON_SELECT_INSERT_OUTPUT,FOLLOW_ON_SELECT_INSERT_OUTPUT_in_onSelectInsertOutput808); 

            match(input, Token.DOWN, null); 
            if ( input.LA(1)==ALL||input.LA(1)==FIRST ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onSelectInsertOutput"


    // $ANTLR start "onSetExpr"
    // EsperEPL2Ast.g:164:1: onSetExpr : ^( ON_SET_EXPR onSetAssignment ( onSetAssignment )* ( whereClause[false] )? ) ;
    public final void onSetExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:165:2: ( ^( ON_SET_EXPR onSetAssignment ( onSetAssignment )* ( whereClause[false] )? ) )
            // EsperEPL2Ast.g:165:4: ^( ON_SET_EXPR onSetAssignment ( onSetAssignment )* ( whereClause[false] )? )
            {
            match(input,ON_SET_EXPR,FOLLOW_ON_SET_EXPR_in_onSetExpr828); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_onSetAssignment_in_onSetExpr830);
            onSetAssignment();

            state._fsp--;

            // EsperEPL2Ast.g:165:34: ( onSetAssignment )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==ON_SET_EXPR_ITEM) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // EsperEPL2Ast.g:165:35: onSetAssignment
            	    {
            	    pushFollow(FOLLOW_onSetAssignment_in_onSetExpr833);
            	    onSetAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);

            // EsperEPL2Ast.g:165:53: ( whereClause[false] )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==WHERE_EXPR) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // EsperEPL2Ast.g:165:53: whereClause[false]
                    {
                    pushFollow(FOLLOW_whereClause_in_onSetExpr837);
                    whereClause(false);

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onSetExpr"


    // $ANTLR start "onUpdateExpr"
    // EsperEPL2Ast.g:168:1: onUpdateExpr : ^( ON_UPDATE_EXPR onExprFrom ( onSetAssignment )+ ( whereClause[false] )? ) ;
    public final void onUpdateExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:169:2: ( ^( ON_UPDATE_EXPR onExprFrom ( onSetAssignment )+ ( whereClause[false] )? ) )
            // EsperEPL2Ast.g:169:4: ^( ON_UPDATE_EXPR onExprFrom ( onSetAssignment )+ ( whereClause[false] )? )
            {
            match(input,ON_UPDATE_EXPR,FOLLOW_ON_UPDATE_EXPR_in_onUpdateExpr852); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_onExprFrom_in_onUpdateExpr854);
            onExprFrom();

            state._fsp--;

            // EsperEPL2Ast.g:169:32: ( onSetAssignment )+
            int cnt54=0;
            loop54:
            do {
                int alt54=2;
                int LA54_0 = input.LA(1);

                if ( (LA54_0==ON_SET_EXPR_ITEM) ) {
                    alt54=1;
                }


                switch (alt54) {
            	case 1 :
            	    // EsperEPL2Ast.g:169:32: onSetAssignment
            	    {
            	    pushFollow(FOLLOW_onSetAssignment_in_onUpdateExpr856);
            	    onSetAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt54 >= 1 ) break loop54;
                        EarlyExitException eee =
                            new EarlyExitException(54, input);
                        throw eee;
                }
                cnt54++;
            } while (true);

            // EsperEPL2Ast.g:169:49: ( whereClause[false] )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==WHERE_EXPR) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // EsperEPL2Ast.g:169:49: whereClause[false]
                    {
                    pushFollow(FOLLOW_whereClause_in_onUpdateExpr859);
                    whereClause(false);

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onUpdateExpr"


    // $ANTLR start "onSetAssignment"
    // EsperEPL2Ast.g:172:1: onSetAssignment : ^( ON_SET_EXPR_ITEM eventPropertyExpr[false] valueExpr ) ;
    public final void onSetAssignment() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:173:2: ( ^( ON_SET_EXPR_ITEM eventPropertyExpr[false] valueExpr ) )
            // EsperEPL2Ast.g:173:4: ^( ON_SET_EXPR_ITEM eventPropertyExpr[false] valueExpr )
            {
            match(input,ON_SET_EXPR_ITEM,FOLLOW_ON_SET_EXPR_ITEM_in_onSetAssignment874); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_eventPropertyExpr_in_onSetAssignment876);
            eventPropertyExpr(false);

            state._fsp--;

            pushFollow(FOLLOW_valueExpr_in_onSetAssignment879);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onSetAssignment"


    // $ANTLR start "onExprFrom"
    // EsperEPL2Ast.g:176:1: onExprFrom : ^( ON_EXPR_FROM IDENT ( IDENT )? ) ;
    public final void onExprFrom() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:177:2: ( ^( ON_EXPR_FROM IDENT ( IDENT )? ) )
            // EsperEPL2Ast.g:177:4: ^( ON_EXPR_FROM IDENT ( IDENT )? )
            {
            match(input,ON_EXPR_FROM,FOLLOW_ON_EXPR_FROM_in_onExprFrom893); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_onExprFrom895); 
            // EsperEPL2Ast.g:177:25: ( IDENT )?
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==IDENT) ) {
                alt56=1;
            }
            switch (alt56) {
                case 1 :
                    // EsperEPL2Ast.g:177:26: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_onExprFrom898); 

                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "onExprFrom"


    // $ANTLR start "createWindowExpr"
    // EsperEPL2Ast.g:180:1: createWindowExpr : ^(i= CREATE_WINDOW_EXPR IDENT ( viewListExpr )? ( RETAINUNION )? ( RETAININTERSECTION )? ( ( ( createSelectionList )? CLASS_IDENT ) | ( createColTypeList ) ) ( createWindowExprInsert )? ) ;
    public final void createWindowExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:181:2: ( ^(i= CREATE_WINDOW_EXPR IDENT ( viewListExpr )? ( RETAINUNION )? ( RETAININTERSECTION )? ( ( ( createSelectionList )? CLASS_IDENT ) | ( createColTypeList ) ) ( createWindowExprInsert )? ) )
            // EsperEPL2Ast.g:181:4: ^(i= CREATE_WINDOW_EXPR IDENT ( viewListExpr )? ( RETAINUNION )? ( RETAININTERSECTION )? ( ( ( createSelectionList )? CLASS_IDENT ) | ( createColTypeList ) ) ( createWindowExprInsert )? )
            {
            i=(CommonTree)match(input,CREATE_WINDOW_EXPR,FOLLOW_CREATE_WINDOW_EXPR_in_createWindowExpr916); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_createWindowExpr918); 
            // EsperEPL2Ast.g:181:33: ( viewListExpr )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==VIEW_EXPR) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // EsperEPL2Ast.g:181:34: viewListExpr
                    {
                    pushFollow(FOLLOW_viewListExpr_in_createWindowExpr921);
                    viewListExpr();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:181:49: ( RETAINUNION )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==RETAINUNION) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // EsperEPL2Ast.g:181:49: RETAINUNION
                    {
                    match(input,RETAINUNION,FOLLOW_RETAINUNION_in_createWindowExpr925); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:181:62: ( RETAININTERSECTION )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==RETAININTERSECTION) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // EsperEPL2Ast.g:181:62: RETAININTERSECTION
                    {
                    match(input,RETAININTERSECTION,FOLLOW_RETAININTERSECTION_in_createWindowExpr928); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:182:4: ( ( ( createSelectionList )? CLASS_IDENT ) | ( createColTypeList ) )
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==CLASS_IDENT||LA61_0==CREATE_WINDOW_SELECT_EXPR) ) {
                alt61=1;
            }
            else if ( (LA61_0==CREATE_COL_TYPE_LIST) ) {
                alt61=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 61, 0, input);

                throw nvae;
            }
            switch (alt61) {
                case 1 :
                    // EsperEPL2Ast.g:183:5: ( ( createSelectionList )? CLASS_IDENT )
                    {
                    // EsperEPL2Ast.g:183:5: ( ( createSelectionList )? CLASS_IDENT )
                    // EsperEPL2Ast.g:183:6: ( createSelectionList )? CLASS_IDENT
                    {
                    // EsperEPL2Ast.g:183:6: ( createSelectionList )?
                    int alt60=2;
                    int LA60_0 = input.LA(1);

                    if ( (LA60_0==CREATE_WINDOW_SELECT_EXPR) ) {
                        alt60=1;
                    }
                    switch (alt60) {
                        case 1 :
                            // EsperEPL2Ast.g:183:6: createSelectionList
                            {
                            pushFollow(FOLLOW_createSelectionList_in_createWindowExpr942);
                            createSelectionList();

                            state._fsp--;


                            }
                            break;

                    }

                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_createWindowExpr945); 

                    }


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:185:12: ( createColTypeList )
                    {
                    // EsperEPL2Ast.g:185:12: ( createColTypeList )
                    // EsperEPL2Ast.g:185:13: createColTypeList
                    {
                    pushFollow(FOLLOW_createColTypeList_in_createWindowExpr974);
                    createColTypeList();

                    state._fsp--;


                    }


                    }
                    break;

            }

            // EsperEPL2Ast.g:187:4: ( createWindowExprInsert )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==INSERT) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // EsperEPL2Ast.g:187:4: createWindowExprInsert
                    {
                    pushFollow(FOLLOW_createWindowExprInsert_in_createWindowExpr985);
                    createWindowExprInsert();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(i); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createWindowExpr"


    // $ANTLR start "createIndexExpr"
    // EsperEPL2Ast.g:191:1: createIndexExpr : ^(i= CREATE_INDEX_EXPR IDENT IDENT indexColList ( IDENT )? ) ;
    public final void createIndexExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:192:2: ( ^(i= CREATE_INDEX_EXPR IDENT IDENT indexColList ( IDENT )? ) )
            // EsperEPL2Ast.g:192:4: ^(i= CREATE_INDEX_EXPR IDENT IDENT indexColList ( IDENT )? )
            {
            i=(CommonTree)match(input,CREATE_INDEX_EXPR,FOLLOW_CREATE_INDEX_EXPR_in_createIndexExpr1005); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_createIndexExpr1007); 
            match(input,IDENT,FOLLOW_IDENT_in_createIndexExpr1009); 
            pushFollow(FOLLOW_indexColList_in_createIndexExpr1011);
            indexColList();

            state._fsp--;

            // EsperEPL2Ast.g:192:51: ( IDENT )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==IDENT) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // EsperEPL2Ast.g:192:51: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_createIndexExpr1013); 

                    }
                    break;

            }

             leaveNode(i); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createIndexExpr"


    // $ANTLR start "indexColList"
    // EsperEPL2Ast.g:195:1: indexColList : ^( INDEXCOL ( indexCol )+ ) ;
    public final void indexColList() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:196:2: ( ^( INDEXCOL ( indexCol )+ ) )
            // EsperEPL2Ast.g:196:4: ^( INDEXCOL ( indexCol )+ )
            {
            match(input,INDEXCOL,FOLLOW_INDEXCOL_in_indexColList1029); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:196:15: ( indexCol )+
            int cnt64=0;
            loop64:
            do {
                int alt64=2;
                int LA64_0 = input.LA(1);

                if ( (LA64_0==INDEXCOL) ) {
                    alt64=1;
                }


                switch (alt64) {
            	case 1 :
            	    // EsperEPL2Ast.g:196:15: indexCol
            	    {
            	    pushFollow(FOLLOW_indexCol_in_indexColList1031);
            	    indexCol();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt64 >= 1 ) break loop64;
                        EarlyExitException eee =
                            new EarlyExitException(64, input);
                        throw eee;
                }
                cnt64++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "indexColList"


    // $ANTLR start "indexCol"
    // EsperEPL2Ast.g:199:1: indexCol : ^( INDEXCOL IDENT ( IDENT )? ) ;
    public final void indexCol() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:200:2: ( ^( INDEXCOL IDENT ( IDENT )? ) )
            // EsperEPL2Ast.g:200:4: ^( INDEXCOL IDENT ( IDENT )? )
            {
            match(input,INDEXCOL,FOLLOW_INDEXCOL_in_indexCol1046); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_indexCol1048); 
            // EsperEPL2Ast.g:200:21: ( IDENT )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==IDENT) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // EsperEPL2Ast.g:200:21: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_indexCol1050); 

                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "indexCol"


    // $ANTLR start "createWindowExprInsert"
    // EsperEPL2Ast.g:203:1: createWindowExprInsert : ^( INSERT ( valueExpr )? ) ;
    public final void createWindowExprInsert() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:204:2: ( ^( INSERT ( valueExpr )? ) )
            // EsperEPL2Ast.g:204:4: ^( INSERT ( valueExpr )? )
            {
            match(input,INSERT,FOLLOW_INSERT_in_createWindowExprInsert1064); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:204:13: ( valueExpr )?
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( ((LA66_0>=IN_SET && LA66_0<=REGEXP)||LA66_0==NOT_EXPR||(LA66_0>=SUM && LA66_0<=AVG)||(LA66_0>=COALESCE && LA66_0<=COUNT)||(LA66_0>=CASE && LA66_0<=CASE2)||LA66_0==ISTREAM||(LA66_0>=PREVIOUS && LA66_0<=EXISTS)||(LA66_0>=INSTANCEOF && LA66_0<=CURRENT_TIMESTAMP)||LA66_0==NEWKW||(LA66_0>=EVAL_AND_EXPR && LA66_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA66_0==EVENT_PROP_EXPR||LA66_0==CONCAT||(LA66_0>=LIB_FUNC_CHAIN && LA66_0<=DOT_EXPR)||LA66_0==ARRAY_EXPR||(LA66_0>=NOT_IN_SET && LA66_0<=NOT_REGEXP)||(LA66_0>=IN_RANGE && LA66_0<=SUBSELECT_EXPR)||(LA66_0>=EXISTS_SUBSELECT_EXPR && LA66_0<=NOT_IN_SUBSELECT_EXPR)||LA66_0==SUBSTITUTION||(LA66_0>=FIRST_AGGREG && LA66_0<=WINDOW_AGGREG)||(LA66_0>=INT_TYPE && LA66_0<=NULL_TYPE)||(LA66_0>=JSON_OBJECT && LA66_0<=JSON_ARRAY)||LA66_0==STAR||(LA66_0>=LT && LA66_0<=GT)||(LA66_0>=BOR && LA66_0<=PLUS)||(LA66_0>=BAND && LA66_0<=BXOR)||(LA66_0>=LE && LA66_0<=GE)||(LA66_0>=MINUS && LA66_0<=MOD)||(LA66_0>=EVAL_IS_GROUP_EXPR && LA66_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt66=1;
                }
                switch (alt66) {
                    case 1 :
                        // EsperEPL2Ast.g:204:13: valueExpr
                        {
                        pushFollow(FOLLOW_valueExpr_in_createWindowExprInsert1066);
                        valueExpr();

                        state._fsp--;


                        }
                        break;

                }


                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createWindowExprInsert"


    // $ANTLR start "createSelectionList"
    // EsperEPL2Ast.g:207:1: createSelectionList : ^(s= CREATE_WINDOW_SELECT_EXPR createSelectionListElement ( createSelectionListElement )* ) ;
    public final void createSelectionList() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:208:2: ( ^(s= CREATE_WINDOW_SELECT_EXPR createSelectionListElement ( createSelectionListElement )* ) )
            // EsperEPL2Ast.g:208:4: ^(s= CREATE_WINDOW_SELECT_EXPR createSelectionListElement ( createSelectionListElement )* )
            {
            s=(CommonTree)match(input,CREATE_WINDOW_SELECT_EXPR,FOLLOW_CREATE_WINDOW_SELECT_EXPR_in_createSelectionList1083); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_createSelectionListElement_in_createSelectionList1085);
            createSelectionListElement();

            state._fsp--;

            // EsperEPL2Ast.g:208:61: ( createSelectionListElement )*
            loop67:
            do {
                int alt67=2;
                int LA67_0 = input.LA(1);

                if ( (LA67_0==SELECTION_ELEMENT_EXPR||LA67_0==WILDCARD_SELECT) ) {
                    alt67=1;
                }


                switch (alt67) {
            	case 1 :
            	    // EsperEPL2Ast.g:208:62: createSelectionListElement
            	    {
            	    pushFollow(FOLLOW_createSelectionListElement_in_createSelectionList1088);
            	    createSelectionListElement();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop67;
                }
            } while (true);

             leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createSelectionList"


    // $ANTLR start "createColTypeList"
    // EsperEPL2Ast.g:211:1: createColTypeList : ^( CREATE_COL_TYPE_LIST createColTypeListElement ( createColTypeListElement )* ) ;
    public final void createColTypeList() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:212:2: ( ^( CREATE_COL_TYPE_LIST createColTypeListElement ( createColTypeListElement )* ) )
            // EsperEPL2Ast.g:212:4: ^( CREATE_COL_TYPE_LIST createColTypeListElement ( createColTypeListElement )* )
            {
            match(input,CREATE_COL_TYPE_LIST,FOLLOW_CREATE_COL_TYPE_LIST_in_createColTypeList1107); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_createColTypeListElement_in_createColTypeList1109);
            createColTypeListElement();

            state._fsp--;

            // EsperEPL2Ast.g:212:52: ( createColTypeListElement )*
            loop68:
            do {
                int alt68=2;
                int LA68_0 = input.LA(1);

                if ( (LA68_0==CREATE_COL_TYPE) ) {
                    alt68=1;
                }


                switch (alt68) {
            	case 1 :
            	    // EsperEPL2Ast.g:212:53: createColTypeListElement
            	    {
            	    pushFollow(FOLLOW_createColTypeListElement_in_createColTypeList1112);
            	    createColTypeListElement();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop68;
                }
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createColTypeList"


    // $ANTLR start "createColTypeListElement"
    // EsperEPL2Ast.g:215:1: createColTypeListElement : ^( CREATE_COL_TYPE CLASS_IDENT CLASS_IDENT ( LBRACK )? ) ;
    public final void createColTypeListElement() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:216:2: ( ^( CREATE_COL_TYPE CLASS_IDENT CLASS_IDENT ( LBRACK )? ) )
            // EsperEPL2Ast.g:216:4: ^( CREATE_COL_TYPE CLASS_IDENT CLASS_IDENT ( LBRACK )? )
            {
            match(input,CREATE_COL_TYPE,FOLLOW_CREATE_COL_TYPE_in_createColTypeListElement1127); 

            match(input, Token.DOWN, null); 
            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_createColTypeListElement1129); 
            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_createColTypeListElement1131); 
            // EsperEPL2Ast.g:216:46: ( LBRACK )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==LBRACK) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // EsperEPL2Ast.g:216:46: LBRACK
                    {
                    match(input,LBRACK,FOLLOW_LBRACK_in_createColTypeListElement1133); 

                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createColTypeListElement"


    // $ANTLR start "createSelectionListElement"
    // EsperEPL2Ast.g:219:1: createSelectionListElement : (w= WILDCARD_SELECT | ^(s= SELECTION_ELEMENT_EXPR ( ( eventPropertyExpr[true] ( IDENT )? ) | ( constant[true] IDENT ) ) ) );
    public final void createSelectionListElement() throws RecognitionException {
        CommonTree w=null;
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:220:2: (w= WILDCARD_SELECT | ^(s= SELECTION_ELEMENT_EXPR ( ( eventPropertyExpr[true] ( IDENT )? ) | ( constant[true] IDENT ) ) ) )
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==WILDCARD_SELECT) ) {
                alt72=1;
            }
            else if ( (LA72_0==SELECTION_ELEMENT_EXPR) ) {
                alt72=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 72, 0, input);

                throw nvae;
            }
            switch (alt72) {
                case 1 :
                    // EsperEPL2Ast.g:220:4: w= WILDCARD_SELECT
                    {
                    w=(CommonTree)match(input,WILDCARD_SELECT,FOLLOW_WILDCARD_SELECT_in_createSelectionListElement1148); 
                     leaveNode(w); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:221:4: ^(s= SELECTION_ELEMENT_EXPR ( ( eventPropertyExpr[true] ( IDENT )? ) | ( constant[true] IDENT ) ) )
                    {
                    s=(CommonTree)match(input,SELECTION_ELEMENT_EXPR,FOLLOW_SELECTION_ELEMENT_EXPR_in_createSelectionListElement1158); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:221:31: ( ( eventPropertyExpr[true] ( IDENT )? ) | ( constant[true] IDENT ) )
                    int alt71=2;
                    int LA71_0 = input.LA(1);

                    if ( (LA71_0==EVENT_PROP_EXPR) ) {
                        alt71=1;
                    }
                    else if ( ((LA71_0>=INT_TYPE && LA71_0<=NULL_TYPE)) ) {
                        alt71=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 71, 0, input);

                        throw nvae;
                    }
                    switch (alt71) {
                        case 1 :
                            // EsperEPL2Ast.g:222:16: ( eventPropertyExpr[true] ( IDENT )? )
                            {
                            // EsperEPL2Ast.g:222:16: ( eventPropertyExpr[true] ( IDENT )? )
                            // EsperEPL2Ast.g:222:17: eventPropertyExpr[true] ( IDENT )?
                            {
                            pushFollow(FOLLOW_eventPropertyExpr_in_createSelectionListElement1178);
                            eventPropertyExpr(true);

                            state._fsp--;

                            // EsperEPL2Ast.g:222:41: ( IDENT )?
                            int alt70=2;
                            int LA70_0 = input.LA(1);

                            if ( (LA70_0==IDENT) ) {
                                alt70=1;
                            }
                            switch (alt70) {
                                case 1 :
                                    // EsperEPL2Ast.g:222:42: IDENT
                                    {
                                    match(input,IDENT,FOLLOW_IDENT_in_createSelectionListElement1182); 

                                    }
                                    break;

                            }


                            }


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:223:16: ( constant[true] IDENT )
                            {
                            // EsperEPL2Ast.g:223:16: ( constant[true] IDENT )
                            // EsperEPL2Ast.g:223:17: constant[true] IDENT
                            {
                            pushFollow(FOLLOW_constant_in_createSelectionListElement1204);
                            constant(true);

                            state._fsp--;

                            match(input,IDENT,FOLLOW_IDENT_in_createSelectionListElement1207); 

                            }


                            }
                            break;

                    }

                     leaveNode(s); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createSelectionListElement"


    // $ANTLR start "createVariableExpr"
    // EsperEPL2Ast.g:227:1: createVariableExpr : ^(i= CREATE_VARIABLE_EXPR CLASS_IDENT IDENT ( IDENT )? ( LBRACK )? ( valueExpr )? ) ;
    public final void createVariableExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:228:2: ( ^(i= CREATE_VARIABLE_EXPR CLASS_IDENT IDENT ( IDENT )? ( LBRACK )? ( valueExpr )? ) )
            // EsperEPL2Ast.g:228:4: ^(i= CREATE_VARIABLE_EXPR CLASS_IDENT IDENT ( IDENT )? ( LBRACK )? ( valueExpr )? )
            {
            i=(CommonTree)match(input,CREATE_VARIABLE_EXPR,FOLLOW_CREATE_VARIABLE_EXPR_in_createVariableExpr1243); 

            match(input, Token.DOWN, null); 
            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_createVariableExpr1245); 
            match(input,IDENT,FOLLOW_IDENT_in_createVariableExpr1247); 
            // EsperEPL2Ast.g:228:47: ( IDENT )?
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==IDENT) ) {
                alt73=1;
            }
            switch (alt73) {
                case 1 :
                    // EsperEPL2Ast.g:228:47: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_createVariableExpr1249); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:228:54: ( LBRACK )?
            int alt74=2;
            int LA74_0 = input.LA(1);

            if ( (LA74_0==LBRACK) ) {
                alt74=1;
            }
            switch (alt74) {
                case 1 :
                    // EsperEPL2Ast.g:228:54: LBRACK
                    {
                    match(input,LBRACK,FOLLOW_LBRACK_in_createVariableExpr1252); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:228:62: ( valueExpr )?
            int alt75=2;
            int LA75_0 = input.LA(1);

            if ( ((LA75_0>=IN_SET && LA75_0<=REGEXP)||LA75_0==NOT_EXPR||(LA75_0>=SUM && LA75_0<=AVG)||(LA75_0>=COALESCE && LA75_0<=COUNT)||(LA75_0>=CASE && LA75_0<=CASE2)||LA75_0==ISTREAM||(LA75_0>=PREVIOUS && LA75_0<=EXISTS)||(LA75_0>=INSTANCEOF && LA75_0<=CURRENT_TIMESTAMP)||LA75_0==NEWKW||(LA75_0>=EVAL_AND_EXPR && LA75_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA75_0==EVENT_PROP_EXPR||LA75_0==CONCAT||(LA75_0>=LIB_FUNC_CHAIN && LA75_0<=DOT_EXPR)||LA75_0==ARRAY_EXPR||(LA75_0>=NOT_IN_SET && LA75_0<=NOT_REGEXP)||(LA75_0>=IN_RANGE && LA75_0<=SUBSELECT_EXPR)||(LA75_0>=EXISTS_SUBSELECT_EXPR && LA75_0<=NOT_IN_SUBSELECT_EXPR)||LA75_0==SUBSTITUTION||(LA75_0>=FIRST_AGGREG && LA75_0<=WINDOW_AGGREG)||(LA75_0>=INT_TYPE && LA75_0<=NULL_TYPE)||(LA75_0>=JSON_OBJECT && LA75_0<=JSON_ARRAY)||LA75_0==STAR||(LA75_0>=LT && LA75_0<=GT)||(LA75_0>=BOR && LA75_0<=PLUS)||(LA75_0>=BAND && LA75_0<=BXOR)||(LA75_0>=LE && LA75_0<=GE)||(LA75_0>=MINUS && LA75_0<=MOD)||(LA75_0>=EVAL_IS_GROUP_EXPR && LA75_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt75=1;
            }
            switch (alt75) {
                case 1 :
                    // EsperEPL2Ast.g:228:62: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_createVariableExpr1255);
                    valueExpr();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(i); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createVariableExpr"


    // $ANTLR start "fafDelete"
    // EsperEPL2Ast.g:231:1: fafDelete : ^(d= DELETE IDENT ( IDENT )? ( whereClause[true] )? ) ;
    public final void fafDelete() throws RecognitionException {
        CommonTree d=null;

        try {
            // EsperEPL2Ast.g:232:2: ( ^(d= DELETE IDENT ( IDENT )? ( whereClause[true] )? ) )
            // EsperEPL2Ast.g:232:4: ^(d= DELETE IDENT ( IDENT )? ( whereClause[true] )? )
            {
            d=(CommonTree)match(input,DELETE,FOLLOW_DELETE_in_fafDelete1274); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_fafDelete1276); 
            // EsperEPL2Ast.g:232:21: ( IDENT )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==IDENT) ) {
                alt76=1;
            }
            switch (alt76) {
                case 1 :
                    // EsperEPL2Ast.g:232:21: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_fafDelete1278); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:232:28: ( whereClause[true] )?
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( (LA77_0==WHERE_EXPR) ) {
                alt77=1;
            }
            switch (alt77) {
                case 1 :
                    // EsperEPL2Ast.g:232:28: whereClause[true]
                    {
                    pushFollow(FOLLOW_whereClause_in_fafDelete1281);
                    whereClause(true);

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(d); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "fafDelete"


    // $ANTLR start "fafUpdate"
    // EsperEPL2Ast.g:235:1: fafUpdate : ^(u= UPDATE updateDetails ) ;
    public final void fafUpdate() throws RecognitionException {
        CommonTree u=null;

        try {
            // EsperEPL2Ast.g:236:2: ( ^(u= UPDATE updateDetails ) )
            // EsperEPL2Ast.g:236:4: ^(u= UPDATE updateDetails )
            {
            u=(CommonTree)match(input,UPDATE,FOLLOW_UPDATE_in_fafUpdate1303); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_updateDetails_in_fafUpdate1305);
            updateDetails();

            state._fsp--;

             leaveNode(u); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "fafUpdate"


    // $ANTLR start "createDataflow"
    // EsperEPL2Ast.g:242:1: createDataflow : ^(s= CREATE_DATAFLOW IDENT ( gop )+ ) ;
    public final void createDataflow() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:243:2: ( ^(s= CREATE_DATAFLOW IDENT ( gop )+ ) )
            // EsperEPL2Ast.g:243:4: ^(s= CREATE_DATAFLOW IDENT ( gop )+ )
            {
            s=(CommonTree)match(input,CREATE_DATAFLOW,FOLLOW_CREATE_DATAFLOW_in_createDataflow1325); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_createDataflow1327); 
            // EsperEPL2Ast.g:243:30: ( gop )+
            int cnt78=0;
            loop78:
            do {
                int alt78=2;
                int LA78_0 = input.LA(1);

                if ( (LA78_0==CREATE_SCHEMA_EXPR||LA78_0==GOP) ) {
                    alt78=1;
                }


                switch (alt78) {
            	case 1 :
            	    // EsperEPL2Ast.g:243:30: gop
            	    {
            	    pushFollow(FOLLOW_gop_in_createDataflow1329);
            	    gop();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt78 >= 1 ) break loop78;
                        EarlyExitException eee =
                            new EarlyExitException(78, input);
                        throw eee;
                }
                cnt78++;
            } while (true);

             leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createDataflow"


    // $ANTLR start "gop"
    // EsperEPL2Ast.g:246:1: gop : ( ^( GOP ( IDENT | SELECT ) ( gopParam )? ( gopOut )? ( gopDetail )? ( annotation[false] )* ) | createSchemaExpr[false] );
    public final void gop() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:247:2: ( ^( GOP ( IDENT | SELECT ) ( gopParam )? ( gopOut )? ( gopDetail )? ( annotation[false] )* ) | createSchemaExpr[false] )
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==GOP) ) {
                alt83=1;
            }
            else if ( (LA83_0==CREATE_SCHEMA_EXPR) ) {
                alt83=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;
            }
            switch (alt83) {
                case 1 :
                    // EsperEPL2Ast.g:247:4: ^( GOP ( IDENT | SELECT ) ( gopParam )? ( gopOut )? ( gopDetail )? ( annotation[false] )* )
                    {
                    match(input,GOP,FOLLOW_GOP_in_gop1346); 

                    match(input, Token.DOWN, null); 
                    if ( input.LA(1)==SELECT||input.LA(1)==IDENT ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // EsperEPL2Ast.g:247:25: ( gopParam )?
                    int alt79=2;
                    int LA79_0 = input.LA(1);

                    if ( (LA79_0==GOPPARAM) ) {
                        alt79=1;
                    }
                    switch (alt79) {
                        case 1 :
                            // EsperEPL2Ast.g:247:25: gopParam
                            {
                            pushFollow(FOLLOW_gopParam_in_gop1354);
                            gopParam();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:247:35: ( gopOut )?
                    int alt80=2;
                    int LA80_0 = input.LA(1);

                    if ( (LA80_0==GOPOUT) ) {
                        alt80=1;
                    }
                    switch (alt80) {
                        case 1 :
                            // EsperEPL2Ast.g:247:35: gopOut
                            {
                            pushFollow(FOLLOW_gopOut_in_gop1357);
                            gopOut();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:247:43: ( gopDetail )?
                    int alt81=2;
                    int LA81_0 = input.LA(1);

                    if ( (LA81_0==GOPCFG) ) {
                        alt81=1;
                    }
                    switch (alt81) {
                        case 1 :
                            // EsperEPL2Ast.g:247:43: gopDetail
                            {
                            pushFollow(FOLLOW_gopDetail_in_gop1360);
                            gopDetail();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:247:54: ( annotation[false] )*
                    loop82:
                    do {
                        int alt82=2;
                        int LA82_0 = input.LA(1);

                        if ( (LA82_0==ANNOTATION) ) {
                            alt82=1;
                        }


                        switch (alt82) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:247:55: annotation[false]
                    	    {
                    	    pushFollow(FOLLOW_annotation_in_gop1364);
                    	    annotation(false);

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop82;
                        }
                    } while (true);


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:248:4: createSchemaExpr[false]
                    {
                    pushFollow(FOLLOW_createSchemaExpr_in_gop1373);
                    createSchemaExpr(false);

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gop"


    // $ANTLR start "gopParam"
    // EsperEPL2Ast.g:251:1: gopParam : ^( GOPPARAM ( gopParamItem )* ) ;
    public final void gopParam() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:252:2: ( ^( GOPPARAM ( gopParamItem )* ) )
            // EsperEPL2Ast.g:252:4: ^( GOPPARAM ( gopParamItem )* )
            {
            match(input,GOPPARAM,FOLLOW_GOPPARAM_in_gopParam1388); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:252:15: ( gopParamItem )*
                loop84:
                do {
                    int alt84=2;
                    int LA84_0 = input.LA(1);

                    if ( (LA84_0==GOPPARAMITM) ) {
                        alt84=1;
                    }


                    switch (alt84) {
                	case 1 :
                	    // EsperEPL2Ast.g:252:15: gopParamItem
                	    {
                	    pushFollow(FOLLOW_gopParamItem_in_gopParam1390);
                	    gopParamItem();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop84;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gopParam"


    // $ANTLR start "gopParamItem"
    // EsperEPL2Ast.g:255:1: gopParamItem : ^( GOPPARAMITM ( CLASS_IDENT )+ ( ^( AS IDENT ) )? ) ;
    public final void gopParamItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:256:2: ( ^( GOPPARAMITM ( CLASS_IDENT )+ ( ^( AS IDENT ) )? ) )
            // EsperEPL2Ast.g:256:4: ^( GOPPARAMITM ( CLASS_IDENT )+ ( ^( AS IDENT ) )? )
            {
            match(input,GOPPARAMITM,FOLLOW_GOPPARAMITM_in_gopParamItem1404); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:256:18: ( CLASS_IDENT )+
            int cnt85=0;
            loop85:
            do {
                int alt85=2;
                int LA85_0 = input.LA(1);

                if ( (LA85_0==CLASS_IDENT) ) {
                    alt85=1;
                }


                switch (alt85) {
            	case 1 :
            	    // EsperEPL2Ast.g:256:18: CLASS_IDENT
            	    {
            	    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_gopParamItem1406); 

            	    }
            	    break;

            	default :
            	    if ( cnt85 >= 1 ) break loop85;
                        EarlyExitException eee =
                            new EarlyExitException(85, input);
                        throw eee;
                }
                cnt85++;
            } while (true);

            // EsperEPL2Ast.g:256:31: ( ^( AS IDENT ) )?
            int alt86=2;
            int LA86_0 = input.LA(1);

            if ( (LA86_0==AS) ) {
                alt86=1;
            }
            switch (alt86) {
                case 1 :
                    // EsperEPL2Ast.g:256:32: ^( AS IDENT )
                    {
                    match(input,AS,FOLLOW_AS_in_gopParamItem1411); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_gopParamItem1413); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gopParamItem"


    // $ANTLR start "gopOut"
    // EsperEPL2Ast.g:259:1: gopOut : ^( GOPOUT ( gopOutItem )* ) ;
    public final void gopOut() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:260:2: ( ^( GOPOUT ( gopOutItem )* ) )
            // EsperEPL2Ast.g:260:4: ^( GOPOUT ( gopOutItem )* )
            {
            match(input,GOPOUT,FOLLOW_GOPOUT_in_gopOut1429); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:260:13: ( gopOutItem )*
                loop87:
                do {
                    int alt87=2;
                    int LA87_0 = input.LA(1);

                    if ( (LA87_0==GOPOUTITM) ) {
                        alt87=1;
                    }


                    switch (alt87) {
                	case 1 :
                	    // EsperEPL2Ast.g:260:13: gopOutItem
                	    {
                	    pushFollow(FOLLOW_gopOutItem_in_gopOut1431);
                	    gopOutItem();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop87;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gopOut"


    // $ANTLR start "gopOutItem"
    // EsperEPL2Ast.g:263:1: gopOutItem : ^( GOPOUTITM CLASS_IDENT ( gopOutTypeParam )* ) ;
    public final void gopOutItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:264:2: ( ^( GOPOUTITM CLASS_IDENT ( gopOutTypeParam )* ) )
            // EsperEPL2Ast.g:264:4: ^( GOPOUTITM CLASS_IDENT ( gopOutTypeParam )* )
            {
            match(input,GOPOUTITM,FOLLOW_GOPOUTITM_in_gopOutItem1445); 

            match(input, Token.DOWN, null); 
            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_gopOutItem1447); 
            // EsperEPL2Ast.g:264:28: ( gopOutTypeParam )*
            loop88:
            do {
                int alt88=2;
                int LA88_0 = input.LA(1);

                if ( (LA88_0==GOPOUTTYP) ) {
                    alt88=1;
                }


                switch (alt88) {
            	case 1 :
            	    // EsperEPL2Ast.g:264:28: gopOutTypeParam
            	    {
            	    pushFollow(FOLLOW_gopOutTypeParam_in_gopOutItem1449);
            	    gopOutTypeParam();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop88;
                }
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gopOutItem"


    // $ANTLR start "gopOutTypeParam"
    // EsperEPL2Ast.g:267:1: gopOutTypeParam : ^( GOPOUTTYP ( ( CLASS_IDENT ( gopOutTypeParam )* ) | QUESTION ) ) ;
    public final void gopOutTypeParam() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:268:2: ( ^( GOPOUTTYP ( ( CLASS_IDENT ( gopOutTypeParam )* ) | QUESTION ) ) )
            // EsperEPL2Ast.g:268:4: ^( GOPOUTTYP ( ( CLASS_IDENT ( gopOutTypeParam )* ) | QUESTION ) )
            {
            match(input,GOPOUTTYP,FOLLOW_GOPOUTTYP_in_gopOutTypeParam1464); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:268:16: ( ( CLASS_IDENT ( gopOutTypeParam )* ) | QUESTION )
            int alt90=2;
            int LA90_0 = input.LA(1);

            if ( (LA90_0==CLASS_IDENT) ) {
                alt90=1;
            }
            else if ( (LA90_0==QUESTION) ) {
                alt90=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 90, 0, input);

                throw nvae;
            }
            switch (alt90) {
                case 1 :
                    // EsperEPL2Ast.g:268:17: ( CLASS_IDENT ( gopOutTypeParam )* )
                    {
                    // EsperEPL2Ast.g:268:17: ( CLASS_IDENT ( gopOutTypeParam )* )
                    // EsperEPL2Ast.g:268:18: CLASS_IDENT ( gopOutTypeParam )*
                    {
                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_gopOutTypeParam1468); 
                    // EsperEPL2Ast.g:268:30: ( gopOutTypeParam )*
                    loop89:
                    do {
                        int alt89=2;
                        int LA89_0 = input.LA(1);

                        if ( (LA89_0==GOPOUTTYP) ) {
                            alt89=1;
                        }


                        switch (alt89) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:268:30: gopOutTypeParam
                    	    {
                    	    pushFollow(FOLLOW_gopOutTypeParam_in_gopOutTypeParam1470);
                    	    gopOutTypeParam();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop89;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:268:50: QUESTION
                    {
                    match(input,QUESTION,FOLLOW_QUESTION_in_gopOutTypeParam1476); 

                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gopOutTypeParam"


    // $ANTLR start "gopDetail"
    // EsperEPL2Ast.g:271:1: gopDetail : ^( GOPCFG ( gopConfig )+ ) ;
    public final void gopDetail() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:272:2: ( ^( GOPCFG ( gopConfig )+ ) )
            // EsperEPL2Ast.g:272:4: ^( GOPCFG ( gopConfig )+ )
            {
            match(input,GOPCFG,FOLLOW_GOPCFG_in_gopDetail1490); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:272:13: ( gopConfig )+
            int cnt91=0;
            loop91:
            do {
                int alt91=2;
                int LA91_0 = input.LA(1);

                if ( ((LA91_0>=GOPCFGITM && LA91_0<=GOPCFGEPL)||LA91_0==GOPCFGEXP) ) {
                    alt91=1;
                }


                switch (alt91) {
            	case 1 :
            	    // EsperEPL2Ast.g:272:13: gopConfig
            	    {
            	    pushFollow(FOLLOW_gopConfig_in_gopDetail1492);
            	    gopConfig();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt91 >= 1 ) break loop91;
                        EarlyExitException eee =
                            new EarlyExitException(91, input);
                        throw eee;
                }
                cnt91++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gopDetail"


    // $ANTLR start "gopConfig"
    // EsperEPL2Ast.g:275:1: gopConfig : ( ^(a= GOPCFGITM IDENT valueExpr ) | ^(b= GOPCFGEXP valueExpr ) | ^(c= GOPCFGEPL selectExpr ) );
    public final void gopConfig() throws RecognitionException {
        CommonTree a=null;
        CommonTree b=null;
        CommonTree c=null;

        try {
            // EsperEPL2Ast.g:276:2: ( ^(a= GOPCFGITM IDENT valueExpr ) | ^(b= GOPCFGEXP valueExpr ) | ^(c= GOPCFGEPL selectExpr ) )
            int alt92=3;
            switch ( input.LA(1) ) {
            case GOPCFGITM:
                {
                alt92=1;
                }
                break;
            case GOPCFGEXP:
                {
                alt92=2;
                }
                break;
            case GOPCFGEPL:
                {
                alt92=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 92, 0, input);

                throw nvae;
            }

            switch (alt92) {
                case 1 :
                    // EsperEPL2Ast.g:276:4: ^(a= GOPCFGITM IDENT valueExpr )
                    {
                    a=(CommonTree)match(input,GOPCFGITM,FOLLOW_GOPCFGITM_in_gopConfig1508); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_gopConfig1510); 
                    pushFollow(FOLLOW_valueExpr_in_gopConfig1512);
                    valueExpr();

                    state._fsp--;

                     leaveNode(a); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:277:4: ^(b= GOPCFGEXP valueExpr )
                    {
                    b=(CommonTree)match(input,GOPCFGEXP,FOLLOW_GOPCFGEXP_in_gopConfig1523); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_gopConfig1525);
                    valueExpr();

                    state._fsp--;

                     leaveNode(b); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:278:4: ^(c= GOPCFGEPL selectExpr )
                    {
                    c=(CommonTree)match(input,GOPCFGEPL,FOLLOW_GOPCFGEPL_in_gopConfig1536); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_selectExpr_in_gopConfig1538);
                    selectExpr();

                    state._fsp--;

                     leaveNode(c); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "gopConfig"


    // $ANTLR start "jsonvalue"
    // EsperEPL2Ast.g:281:1: jsonvalue : ( constant[false] | jsonobject[false] | jsonarray[false] );
    public final void jsonvalue() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:282:2: ( constant[false] | jsonobject[false] | jsonarray[false] )
            int alt93=3;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt93=1;
                }
                break;
            case JSON_OBJECT:
                {
                alt93=2;
                }
                break;
            case JSON_ARRAY:
                {
                alt93=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 93, 0, input);

                throw nvae;
            }

            switch (alt93) {
                case 1 :
                    // EsperEPL2Ast.g:282:5: constant[false]
                    {
                    pushFollow(FOLLOW_constant_in_jsonvalue1554);
                    constant(false);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:283:5: jsonobject[false]
                    {
                    pushFollow(FOLLOW_jsonobject_in_jsonvalue1561);
                    jsonobject(false);

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:284:5: jsonarray[false]
                    {
                    pushFollow(FOLLOW_jsonarray_in_jsonvalue1568);
                    jsonarray(false);

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "jsonvalue"


    // $ANTLR start "jsonobject"
    // EsperEPL2Ast.g:287:1: jsonobject[boolean isLeaveNode] : ^(o= JSON_OBJECT ( jsonpair )* ) ;
    public final void jsonobject(boolean isLeaveNode) throws RecognitionException {
        CommonTree o=null;

        try {
            // EsperEPL2Ast.g:288:2: ( ^(o= JSON_OBJECT ( jsonpair )* ) )
            // EsperEPL2Ast.g:288:5: ^(o= JSON_OBJECT ( jsonpair )* )
            {
            o=(CommonTree)match(input,JSON_OBJECT,FOLLOW_JSON_OBJECT_in_jsonobject1585); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:288:21: ( jsonpair )*
                loop94:
                do {
                    int alt94=2;
                    int LA94_0 = input.LA(1);

                    if ( (LA94_0==JSON_FIELD) ) {
                        alt94=1;
                    }


                    switch (alt94) {
                	case 1 :
                	    // EsperEPL2Ast.g:288:21: jsonpair
                	    {
                	    pushFollow(FOLLOW_jsonpair_in_jsonobject1587);
                	    jsonpair();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop94;
                    }
                } while (true);

                 if (isLeaveNode) leaveNode(o); 

                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "jsonobject"


    // $ANTLR start "jsonarray"
    // EsperEPL2Ast.g:291:1: jsonarray[boolean isLeaveNode] : ^(a= JSON_ARRAY ( jsonvalue )* ) ;
    public final void jsonarray(boolean isLeaveNode) throws RecognitionException {
        CommonTree a=null;

        try {
            // EsperEPL2Ast.g:292:9: ( ^(a= JSON_ARRAY ( jsonvalue )* ) )
            // EsperEPL2Ast.g:292:12: ^(a= JSON_ARRAY ( jsonvalue )* )
            {
            a=(CommonTree)match(input,JSON_ARRAY,FOLLOW_JSON_ARRAY_in_jsonarray1616); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:292:27: ( jsonvalue )*
                loop95:
                do {
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( ((LA95_0>=INT_TYPE && LA95_0<=NULL_TYPE)||(LA95_0>=JSON_OBJECT && LA95_0<=JSON_ARRAY)) ) {
                        alt95=1;
                    }


                    switch (alt95) {
                	case 1 :
                	    // EsperEPL2Ast.g:292:27: jsonvalue
                	    {
                	    pushFollow(FOLLOW_jsonvalue_in_jsonarray1618);
                	    jsonvalue();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop95;
                    }
                } while (true);

                 if (isLeaveNode) leaveNode(a); 

                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "jsonarray"


    // $ANTLR start "jsonpair"
    // EsperEPL2Ast.g:295:1: jsonpair : ^( JSON_FIELD ( constant[false] | IDENT ) jsonvalue ) ;
    public final void jsonpair() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:296:2: ( ^( JSON_FIELD ( constant[false] | IDENT ) jsonvalue ) )
            // EsperEPL2Ast.g:296:5: ^( JSON_FIELD ( constant[false] | IDENT ) jsonvalue )
            {
            match(input,JSON_FIELD,FOLLOW_JSON_FIELD_in_jsonpair1636); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:296:18: ( constant[false] | IDENT )
            int alt96=2;
            int LA96_0 = input.LA(1);

            if ( ((LA96_0>=INT_TYPE && LA96_0<=NULL_TYPE)) ) {
                alt96=1;
            }
            else if ( (LA96_0==IDENT) ) {
                alt96=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 96, 0, input);

                throw nvae;
            }
            switch (alt96) {
                case 1 :
                    // EsperEPL2Ast.g:296:19: constant[false]
                    {
                    pushFollow(FOLLOW_constant_in_jsonpair1639);
                    constant(false);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:296:37: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_jsonpair1644); 

                    }
                    break;

            }

            pushFollow(FOLLOW_jsonvalue_in_jsonpair1647);
            jsonvalue();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "jsonpair"


    // $ANTLR start "createContextExpr"
    // EsperEPL2Ast.g:302:1: createContextExpr : ^(s= CREATE_CTX IDENT createContextDetail ) ;
    public final void createContextExpr() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:303:2: ( ^(s= CREATE_CTX IDENT createContextDetail ) )
            // EsperEPL2Ast.g:303:4: ^(s= CREATE_CTX IDENT createContextDetail )
            {
            s=(CommonTree)match(input,CREATE_CTX,FOLLOW_CREATE_CTX_in_createContextExpr1667); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_createContextExpr1669); 
            pushFollow(FOLLOW_createContextDetail_in_createContextExpr1671);
            createContextDetail();

            state._fsp--;

             leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextExpr"


    // $ANTLR start "createContextDetail"
    // EsperEPL2Ast.g:306:1: createContextDetail : ( ^( CREATE_CTX_FIXED createContextRangePoint createContextRangePoint ) | ^( CREATE_CTX_INIT createContextRangePoint createContextRangePoint ) | ^( CREATE_CTX_PART ( createContextPartitionItem )+ ) | ^( CREATE_CTX_CAT ( createContextCategoryItem )+ eventFilterExpr[false] ) | ^( CREATE_CTX_COAL ( createContextCoalesceItem )+ IDENT number ( IDENT )? ) | ^( CREATE_CTX_NESTED createContextNested ( createContextNested )+ ) );
    public final void createContextDetail() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:307:2: ( ^( CREATE_CTX_FIXED createContextRangePoint createContextRangePoint ) | ^( CREATE_CTX_INIT createContextRangePoint createContextRangePoint ) | ^( CREATE_CTX_PART ( createContextPartitionItem )+ ) | ^( CREATE_CTX_CAT ( createContextCategoryItem )+ eventFilterExpr[false] ) | ^( CREATE_CTX_COAL ( createContextCoalesceItem )+ IDENT number ( IDENT )? ) | ^( CREATE_CTX_NESTED createContextNested ( createContextNested )+ ) )
            int alt102=6;
            switch ( input.LA(1) ) {
            case CREATE_CTX_FIXED:
                {
                alt102=1;
                }
                break;
            case CREATE_CTX_INIT:
                {
                alt102=2;
                }
                break;
            case CREATE_CTX_PART:
                {
                alt102=3;
                }
                break;
            case CREATE_CTX_CAT:
                {
                alt102=4;
                }
                break;
            case CREATE_CTX_COAL:
                {
                alt102=5;
                }
                break;
            case CREATE_CTX_NESTED:
                {
                alt102=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 102, 0, input);

                throw nvae;
            }

            switch (alt102) {
                case 1 :
                    // EsperEPL2Ast.g:307:4: ^( CREATE_CTX_FIXED createContextRangePoint createContextRangePoint )
                    {
                    match(input,CREATE_CTX_FIXED,FOLLOW_CREATE_CTX_FIXED_in_createContextDetail1687); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_createContextRangePoint_in_createContextDetail1689);
                    createContextRangePoint();

                    state._fsp--;

                    pushFollow(FOLLOW_createContextRangePoint_in_createContextDetail1691);
                    createContextRangePoint();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:308:4: ^( CREATE_CTX_INIT createContextRangePoint createContextRangePoint )
                    {
                    match(input,CREATE_CTX_INIT,FOLLOW_CREATE_CTX_INIT_in_createContextDetail1698); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_createContextRangePoint_in_createContextDetail1700);
                    createContextRangePoint();

                    state._fsp--;

                    pushFollow(FOLLOW_createContextRangePoint_in_createContextDetail1702);
                    createContextRangePoint();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:309:4: ^( CREATE_CTX_PART ( createContextPartitionItem )+ )
                    {
                    match(input,CREATE_CTX_PART,FOLLOW_CREATE_CTX_PART_in_createContextDetail1709); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:309:22: ( createContextPartitionItem )+
                    int cnt97=0;
                    loop97:
                    do {
                        int alt97=2;
                        int LA97_0 = input.LA(1);

                        if ( (LA97_0==PARTITIONITEM) ) {
                            alt97=1;
                        }


                        switch (alt97) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:309:22: createContextPartitionItem
                    	    {
                    	    pushFollow(FOLLOW_createContextPartitionItem_in_createContextDetail1711);
                    	    createContextPartitionItem();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt97 >= 1 ) break loop97;
                                EarlyExitException eee =
                                    new EarlyExitException(97, input);
                                throw eee;
                        }
                        cnt97++;
                    } while (true);


                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:310:4: ^( CREATE_CTX_CAT ( createContextCategoryItem )+ eventFilterExpr[false] )
                    {
                    match(input,CREATE_CTX_CAT,FOLLOW_CREATE_CTX_CAT_in_createContextDetail1719); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:310:21: ( createContextCategoryItem )+
                    int cnt98=0;
                    loop98:
                    do {
                        int alt98=2;
                        int LA98_0 = input.LA(1);

                        if ( (LA98_0==CREATE_CTX_CATITEM) ) {
                            alt98=1;
                        }


                        switch (alt98) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:310:21: createContextCategoryItem
                    	    {
                    	    pushFollow(FOLLOW_createContextCategoryItem_in_createContextDetail1721);
                    	    createContextCategoryItem();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt98 >= 1 ) break loop98;
                                EarlyExitException eee =
                                    new EarlyExitException(98, input);
                                throw eee;
                        }
                        cnt98++;
                    } while (true);

                    pushFollow(FOLLOW_eventFilterExpr_in_createContextDetail1724);
                    eventFilterExpr(false);

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:311:4: ^( CREATE_CTX_COAL ( createContextCoalesceItem )+ IDENT number ( IDENT )? )
                    {
                    match(input,CREATE_CTX_COAL,FOLLOW_CREATE_CTX_COAL_in_createContextDetail1732); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:311:22: ( createContextCoalesceItem )+
                    int cnt99=0;
                    loop99:
                    do {
                        int alt99=2;
                        int LA99_0 = input.LA(1);

                        if ( (LA99_0==COALESCE) ) {
                            alt99=1;
                        }


                        switch (alt99) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:311:22: createContextCoalesceItem
                    	    {
                    	    pushFollow(FOLLOW_createContextCoalesceItem_in_createContextDetail1734);
                    	    createContextCoalesceItem();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt99 >= 1 ) break loop99;
                                EarlyExitException eee =
                                    new EarlyExitException(99, input);
                                throw eee;
                        }
                        cnt99++;
                    } while (true);

                    match(input,IDENT,FOLLOW_IDENT_in_createContextDetail1737); 
                    pushFollow(FOLLOW_number_in_createContextDetail1739);
                    number();

                    state._fsp--;

                    // EsperEPL2Ast.g:311:62: ( IDENT )?
                    int alt100=2;
                    int LA100_0 = input.LA(1);

                    if ( (LA100_0==IDENT) ) {
                        alt100=1;
                    }
                    switch (alt100) {
                        case 1 :
                            // EsperEPL2Ast.g:311:62: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_createContextDetail1741); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:312:4: ^( CREATE_CTX_NESTED createContextNested ( createContextNested )+ )
                    {
                    match(input,CREATE_CTX_NESTED,FOLLOW_CREATE_CTX_NESTED_in_createContextDetail1749); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_createContextNested_in_createContextDetail1751);
                    createContextNested();

                    state._fsp--;

                    // EsperEPL2Ast.g:312:44: ( createContextNested )+
                    int cnt101=0;
                    loop101:
                    do {
                        int alt101=2;
                        int LA101_0 = input.LA(1);

                        if ( (LA101_0==CREATE_CTX) ) {
                            alt101=1;
                        }


                        switch (alt101) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:312:44: createContextNested
                    	    {
                    	    pushFollow(FOLLOW_createContextNested_in_createContextDetail1753);
                    	    createContextNested();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt101 >= 1 ) break loop101;
                                EarlyExitException eee =
                                    new EarlyExitException(101, input);
                                throw eee;
                        }
                        cnt101++;
                    } while (true);


                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextDetail"


    // $ANTLR start "createContextRangePoint"
    // EsperEPL2Ast.g:315:1: createContextRangePoint : ( createContextFilter | ^( CREATE_CTX_PATTERN patternInclusionExpression ( IDENT )? ) | crontabLimitParameterSet | ^( AFTER timePeriod ) );
    public final void createContextRangePoint() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:316:2: ( createContextFilter | ^( CREATE_CTX_PATTERN patternInclusionExpression ( IDENT )? ) | crontabLimitParameterSet | ^( AFTER timePeriod ) )
            int alt104=4;
            switch ( input.LA(1) ) {
            case STREAM_EXPR:
                {
                alt104=1;
                }
                break;
            case CREATE_CTX_PATTERN:
                {
                alt104=2;
                }
                break;
            case CRONTAB_LIMIT_EXPR_PARAM:
                {
                alt104=3;
                }
                break;
            case AFTER:
                {
                alt104=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 104, 0, input);

                throw nvae;
            }

            switch (alt104) {
                case 1 :
                    // EsperEPL2Ast.g:316:4: createContextFilter
                    {
                    pushFollow(FOLLOW_createContextFilter_in_createContextRangePoint1766);
                    createContextFilter();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:317:5: ^( CREATE_CTX_PATTERN patternInclusionExpression ( IDENT )? )
                    {
                    match(input,CREATE_CTX_PATTERN,FOLLOW_CREATE_CTX_PATTERN_in_createContextRangePoint1774); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_patternInclusionExpression_in_createContextRangePoint1776);
                    patternInclusionExpression();

                    state._fsp--;

                    // EsperEPL2Ast.g:317:53: ( IDENT )?
                    int alt103=2;
                    int LA103_0 = input.LA(1);

                    if ( (LA103_0==IDENT) ) {
                        alt103=1;
                    }
                    switch (alt103) {
                        case 1 :
                            // EsperEPL2Ast.g:317:53: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_createContextRangePoint1778); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:318:4: crontabLimitParameterSet
                    {
                    pushFollow(FOLLOW_crontabLimitParameterSet_in_createContextRangePoint1785);
                    crontabLimitParameterSet();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:319:4: ^( AFTER timePeriod )
                    {
                    match(input,AFTER,FOLLOW_AFTER_in_createContextRangePoint1791); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_timePeriod_in_createContextRangePoint1793);
                    timePeriod();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextRangePoint"


    // $ANTLR start "createContextNested"
    // EsperEPL2Ast.g:322:1: createContextNested : ^(s= CREATE_CTX IDENT createContextDetail ) ;
    public final void createContextNested() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:323:2: ( ^(s= CREATE_CTX IDENT createContextDetail ) )
            // EsperEPL2Ast.g:323:4: ^(s= CREATE_CTX IDENT createContextDetail )
            {
            s=(CommonTree)match(input,CREATE_CTX,FOLLOW_CREATE_CTX_in_createContextNested1809); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_createContextNested1811); 
            pushFollow(FOLLOW_createContextDetail_in_createContextNested1813);
            createContextDetail();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextNested"


    // $ANTLR start "createContextFilter"
    // EsperEPL2Ast.g:326:1: createContextFilter : ^( STREAM_EXPR eventFilterExpr[false] ( IDENT )? ) ;
    public final void createContextFilter() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:327:2: ( ^( STREAM_EXPR eventFilterExpr[false] ( IDENT )? ) )
            // EsperEPL2Ast.g:327:4: ^( STREAM_EXPR eventFilterExpr[false] ( IDENT )? )
            {
            match(input,STREAM_EXPR,FOLLOW_STREAM_EXPR_in_createContextFilter1826); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_eventFilterExpr_in_createContextFilter1828);
            eventFilterExpr(false);

            state._fsp--;

            // EsperEPL2Ast.g:327:41: ( IDENT )?
            int alt105=2;
            int LA105_0 = input.LA(1);

            if ( (LA105_0==IDENT) ) {
                alt105=1;
            }
            switch (alt105) {
                case 1 :
                    // EsperEPL2Ast.g:327:41: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_createContextFilter1831); 

                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextFilter"


    // $ANTLR start "createContextPartitionItem"
    // EsperEPL2Ast.g:330:1: createContextPartitionItem : ^( PARTITIONITEM eventFilterExpr[false] ( eventPropertyExpr[false] )+ ) ;
    public final void createContextPartitionItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:331:2: ( ^( PARTITIONITEM eventFilterExpr[false] ( eventPropertyExpr[false] )+ ) )
            // EsperEPL2Ast.g:331:4: ^( PARTITIONITEM eventFilterExpr[false] ( eventPropertyExpr[false] )+ )
            {
            match(input,PARTITIONITEM,FOLLOW_PARTITIONITEM_in_createContextPartitionItem1847); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_eventFilterExpr_in_createContextPartitionItem1849);
            eventFilterExpr(false);

            state._fsp--;

            // EsperEPL2Ast.g:331:43: ( eventPropertyExpr[false] )+
            int cnt106=0;
            loop106:
            do {
                int alt106=2;
                int LA106_0 = input.LA(1);

                if ( (LA106_0==EVENT_PROP_EXPR) ) {
                    alt106=1;
                }


                switch (alt106) {
            	case 1 :
            	    // EsperEPL2Ast.g:331:43: eventPropertyExpr[false]
            	    {
            	    pushFollow(FOLLOW_eventPropertyExpr_in_createContextPartitionItem1852);
            	    eventPropertyExpr(false);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt106 >= 1 ) break loop106;
                        EarlyExitException eee =
                            new EarlyExitException(106, input);
                        throw eee;
                }
                cnt106++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextPartitionItem"


    // $ANTLR start "createContextCoalesceItem"
    // EsperEPL2Ast.g:334:1: createContextCoalesceItem : ^( COALESCE libFunctionWithClass eventFilterExpr[false] ) ;
    public final void createContextCoalesceItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:335:2: ( ^( COALESCE libFunctionWithClass eventFilterExpr[false] ) )
            // EsperEPL2Ast.g:335:4: ^( COALESCE libFunctionWithClass eventFilterExpr[false] )
            {
            match(input,COALESCE,FOLLOW_COALESCE_in_createContextCoalesceItem1869); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_libFunctionWithClass_in_createContextCoalesceItem1871);
            libFunctionWithClass();

            state._fsp--;

            pushFollow(FOLLOW_eventFilterExpr_in_createContextCoalesceItem1873);
            eventFilterExpr(false);

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextCoalesceItem"


    // $ANTLR start "createContextCategoryItem"
    // EsperEPL2Ast.g:338:1: createContextCategoryItem : ^( CREATE_CTX_CATITEM valueExpr IDENT ) ;
    public final void createContextCategoryItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:339:2: ( ^( CREATE_CTX_CATITEM valueExpr IDENT ) )
            // EsperEPL2Ast.g:339:4: ^( CREATE_CTX_CATITEM valueExpr IDENT )
            {
            match(input,CREATE_CTX_CATITEM,FOLLOW_CREATE_CTX_CATITEM_in_createContextCategoryItem1889); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_createContextCategoryItem1891);
            valueExpr();

            state._fsp--;

            match(input,IDENT,FOLLOW_IDENT_in_createContextCategoryItem1893); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createContextCategoryItem"


    // $ANTLR start "createExpr"
    // EsperEPL2Ast.g:342:1: createExpr : ^(s= CREATE_EXPR expressionDecl[false] ) ;
    public final void createExpr() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:343:2: ( ^(s= CREATE_EXPR expressionDecl[false] ) )
            // EsperEPL2Ast.g:343:4: ^(s= CREATE_EXPR expressionDecl[false] )
            {
            s=(CommonTree)match(input,CREATE_EXPR,FOLLOW_CREATE_EXPR_in_createExpr1910); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_expressionDecl_in_createExpr1912);
            expressionDecl(false);

            state._fsp--;

             leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createExpr"


    // $ANTLR start "createSchemaExpr"
    // EsperEPL2Ast.g:346:1: createSchemaExpr[boolean isLeaveNode] : ^(s= CREATE_SCHEMA_EXPR createSchemaDef ( IDENT )? ) ;
    public final void createSchemaExpr(boolean isLeaveNode) throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:347:2: ( ^(s= CREATE_SCHEMA_EXPR createSchemaDef ( IDENT )? ) )
            // EsperEPL2Ast.g:347:4: ^(s= CREATE_SCHEMA_EXPR createSchemaDef ( IDENT )? )
            {
            s=(CommonTree)match(input,CREATE_SCHEMA_EXPR,FOLLOW_CREATE_SCHEMA_EXPR_in_createSchemaExpr1931); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_createSchemaDef_in_createSchemaExpr1933);
            createSchemaDef();

            state._fsp--;

            // EsperEPL2Ast.g:347:43: ( IDENT )?
            int alt107=2;
            int LA107_0 = input.LA(1);

            if ( (LA107_0==IDENT) ) {
                alt107=1;
            }
            switch (alt107) {
                case 1 :
                    // EsperEPL2Ast.g:347:43: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_createSchemaExpr1935); 

                    }
                    break;

            }

             if (isLeaveNode) leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createSchemaExpr"


    // $ANTLR start "createSchemaDef"
    // EsperEPL2Ast.g:350:1: createSchemaDef : ^( CREATE_SCHEMA_DEF IDENT ( variantList | ( createColTypeList )? ) ( createSchemaQual )* ) ;
    public final void createSchemaDef() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:351:2: ( ^( CREATE_SCHEMA_DEF IDENT ( variantList | ( createColTypeList )? ) ( createSchemaQual )* ) )
            // EsperEPL2Ast.g:351:4: ^( CREATE_SCHEMA_DEF IDENT ( variantList | ( createColTypeList )? ) ( createSchemaQual )* )
            {
            match(input,CREATE_SCHEMA_DEF,FOLLOW_CREATE_SCHEMA_DEF_in_createSchemaDef1952); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_createSchemaDef1954); 
            // EsperEPL2Ast.g:351:30: ( variantList | ( createColTypeList )? )
            int alt109=2;
            int LA109_0 = input.LA(1);

            if ( (LA109_0==VARIANT_LIST) ) {
                alt109=1;
            }
            else if ( (LA109_0==UP||LA109_0==CREATE_COL_TYPE_LIST||LA109_0==CREATE_SCHEMA_EXPR_QUAL) ) {
                alt109=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 109, 0, input);

                throw nvae;
            }
            switch (alt109) {
                case 1 :
                    // EsperEPL2Ast.g:351:31: variantList
                    {
                    pushFollow(FOLLOW_variantList_in_createSchemaDef1957);
                    variantList();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:351:43: ( createColTypeList )?
                    {
                    // EsperEPL2Ast.g:351:43: ( createColTypeList )?
                    int alt108=2;
                    int LA108_0 = input.LA(1);

                    if ( (LA108_0==CREATE_COL_TYPE_LIST) ) {
                        alt108=1;
                    }
                    switch (alt108) {
                        case 1 :
                            // EsperEPL2Ast.g:351:43: createColTypeList
                            {
                            pushFollow(FOLLOW_createColTypeList_in_createSchemaDef1959);
                            createColTypeList();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;

            }

            // EsperEPL2Ast.g:351:63: ( createSchemaQual )*
            loop110:
            do {
                int alt110=2;
                int LA110_0 = input.LA(1);

                if ( (LA110_0==CREATE_SCHEMA_EXPR_QUAL) ) {
                    alt110=1;
                }


                switch (alt110) {
            	case 1 :
            	    // EsperEPL2Ast.g:351:63: createSchemaQual
            	    {
            	    pushFollow(FOLLOW_createSchemaQual_in_createSchemaDef1963);
            	    createSchemaQual();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop110;
                }
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createSchemaDef"


    // $ANTLR start "createSchemaQual"
    // EsperEPL2Ast.g:354:1: createSchemaQual : ^( CREATE_SCHEMA_EXPR_QUAL IDENT exprCol ) ;
    public final void createSchemaQual() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:355:2: ( ^( CREATE_SCHEMA_EXPR_QUAL IDENT exprCol ) )
            // EsperEPL2Ast.g:355:4: ^( CREATE_SCHEMA_EXPR_QUAL IDENT exprCol )
            {
            match(input,CREATE_SCHEMA_EXPR_QUAL,FOLLOW_CREATE_SCHEMA_EXPR_QUAL_in_createSchemaQual1978); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_createSchemaQual1980); 
            pushFollow(FOLLOW_exprCol_in_createSchemaQual1982);
            exprCol();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "createSchemaQual"


    // $ANTLR start "variantList"
    // EsperEPL2Ast.g:358:1: variantList : ^( VARIANT_LIST ( STAR | CLASS_IDENT )+ ) ;
    public final void variantList() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:359:2: ( ^( VARIANT_LIST ( STAR | CLASS_IDENT )+ ) )
            // EsperEPL2Ast.g:359:4: ^( VARIANT_LIST ( STAR | CLASS_IDENT )+ )
            {
            match(input,VARIANT_LIST,FOLLOW_VARIANT_LIST_in_variantList1998); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:359:19: ( STAR | CLASS_IDENT )+
            int cnt111=0;
            loop111:
            do {
                int alt111=2;
                int LA111_0 = input.LA(1);

                if ( (LA111_0==CLASS_IDENT||LA111_0==STAR) ) {
                    alt111=1;
                }


                switch (alt111) {
            	case 1 :
            	    // EsperEPL2Ast.g:
            	    {
            	    if ( input.LA(1)==CLASS_IDENT||input.LA(1)==STAR ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt111 >= 1 ) break loop111;
                        EarlyExitException eee =
                            new EarlyExitException(111, input);
                        throw eee;
                }
                cnt111++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "variantList"


    // $ANTLR start "selectExpr"
    // EsperEPL2Ast.g:362:1: selectExpr : ( insertIntoExpr )? selectClause fromClause ( matchRecogClause )? ( whereClause[true] )? ( groupByClause )? ( havingClause )? ( outputLimitExpr )? ( orderByClause )? ( rowLimitClause )? ;
    public final void selectExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:363:2: ( ( insertIntoExpr )? selectClause fromClause ( matchRecogClause )? ( whereClause[true] )? ( groupByClause )? ( havingClause )? ( outputLimitExpr )? ( orderByClause )? ( rowLimitClause )? )
            // EsperEPL2Ast.g:363:4: ( insertIntoExpr )? selectClause fromClause ( matchRecogClause )? ( whereClause[true] )? ( groupByClause )? ( havingClause )? ( outputLimitExpr )? ( orderByClause )? ( rowLimitClause )?
            {
            // EsperEPL2Ast.g:363:4: ( insertIntoExpr )?
            int alt112=2;
            int LA112_0 = input.LA(1);

            if ( (LA112_0==INSERTINTO_EXPR) ) {
                alt112=1;
            }
            switch (alt112) {
                case 1 :
                    // EsperEPL2Ast.g:363:5: insertIntoExpr
                    {
                    pushFollow(FOLLOW_insertIntoExpr_in_selectExpr2018);
                    insertIntoExpr();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_selectClause_in_selectExpr2024);
            selectClause();

            state._fsp--;

            pushFollow(FOLLOW_fromClause_in_selectExpr2029);
            fromClause();

            state._fsp--;

            // EsperEPL2Ast.g:366:3: ( matchRecogClause )?
            int alt113=2;
            int LA113_0 = input.LA(1);

            if ( (LA113_0==MATCH_RECOGNIZE) ) {
                alt113=1;
            }
            switch (alt113) {
                case 1 :
                    // EsperEPL2Ast.g:366:4: matchRecogClause
                    {
                    pushFollow(FOLLOW_matchRecogClause_in_selectExpr2034);
                    matchRecogClause();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:367:3: ( whereClause[true] )?
            int alt114=2;
            int LA114_0 = input.LA(1);

            if ( (LA114_0==WHERE_EXPR) ) {
                alt114=1;
            }
            switch (alt114) {
                case 1 :
                    // EsperEPL2Ast.g:367:4: whereClause[true]
                    {
                    pushFollow(FOLLOW_whereClause_in_selectExpr2041);
                    whereClause(true);

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:368:3: ( groupByClause )?
            int alt115=2;
            int LA115_0 = input.LA(1);

            if ( (LA115_0==GROUP_BY_EXPR) ) {
                alt115=1;
            }
            switch (alt115) {
                case 1 :
                    // EsperEPL2Ast.g:368:4: groupByClause
                    {
                    pushFollow(FOLLOW_groupByClause_in_selectExpr2049);
                    groupByClause();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:369:3: ( havingClause )?
            int alt116=2;
            int LA116_0 = input.LA(1);

            if ( (LA116_0==HAVING_EXPR) ) {
                alt116=1;
            }
            switch (alt116) {
                case 1 :
                    // EsperEPL2Ast.g:369:4: havingClause
                    {
                    pushFollow(FOLLOW_havingClause_in_selectExpr2056);
                    havingClause();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:370:3: ( outputLimitExpr )?
            int alt117=2;
            int LA117_0 = input.LA(1);

            if ( ((LA117_0>=EVENT_LIMIT_EXPR && LA117_0<=CRONTAB_LIMIT_EXPR)||(LA117_0>=WHEN_LIMIT_EXPR && LA117_0<=TERM_LIMIT_EXPR)) ) {
                alt117=1;
            }
            switch (alt117) {
                case 1 :
                    // EsperEPL2Ast.g:370:4: outputLimitExpr
                    {
                    pushFollow(FOLLOW_outputLimitExpr_in_selectExpr2063);
                    outputLimitExpr();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:371:3: ( orderByClause )?
            int alt118=2;
            int LA118_0 = input.LA(1);

            if ( (LA118_0==ORDER_BY_EXPR) ) {
                alt118=1;
            }
            switch (alt118) {
                case 1 :
                    // EsperEPL2Ast.g:371:4: orderByClause
                    {
                    pushFollow(FOLLOW_orderByClause_in_selectExpr2070);
                    orderByClause();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:372:3: ( rowLimitClause )?
            int alt119=2;
            int LA119_0 = input.LA(1);

            if ( (LA119_0==ROW_LIMIT_EXPR) ) {
                alt119=1;
            }
            switch (alt119) {
                case 1 :
                    // EsperEPL2Ast.g:372:4: rowLimitClause
                    {
                    pushFollow(FOLLOW_rowLimitClause_in_selectExpr2077);
                    rowLimitClause();

                    state._fsp--;


                    }
                    break;

            }


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectExpr"


    // $ANTLR start "insertIntoExpr"
    // EsperEPL2Ast.g:375:1: insertIntoExpr : ^(i= INSERTINTO_EXPR ( ISTREAM | RSTREAM | IRSTREAM )? CLASS_IDENT ( exprCol )? ) ;
    public final void insertIntoExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:376:2: ( ^(i= INSERTINTO_EXPR ( ISTREAM | RSTREAM | IRSTREAM )? CLASS_IDENT ( exprCol )? ) )
            // EsperEPL2Ast.g:376:4: ^(i= INSERTINTO_EXPR ( ISTREAM | RSTREAM | IRSTREAM )? CLASS_IDENT ( exprCol )? )
            {
            i=(CommonTree)match(input,INSERTINTO_EXPR,FOLLOW_INSERTINTO_EXPR_in_insertIntoExpr2094); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:376:24: ( ISTREAM | RSTREAM | IRSTREAM )?
            int alt120=2;
            int LA120_0 = input.LA(1);

            if ( ((LA120_0>=RSTREAM && LA120_0<=IRSTREAM)) ) {
                alt120=1;
            }
            switch (alt120) {
                case 1 :
                    // EsperEPL2Ast.g:
                    {
                    if ( (input.LA(1)>=RSTREAM && input.LA(1)<=IRSTREAM) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }

            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_insertIntoExpr2109); 
            // EsperEPL2Ast.g:376:68: ( exprCol )?
            int alt121=2;
            int LA121_0 = input.LA(1);

            if ( (LA121_0==EXPRCOL) ) {
                alt121=1;
            }
            switch (alt121) {
                case 1 :
                    // EsperEPL2Ast.g:376:69: exprCol
                    {
                    pushFollow(FOLLOW_exprCol_in_insertIntoExpr2112);
                    exprCol();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(i); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "insertIntoExpr"


    // $ANTLR start "exprCol"
    // EsperEPL2Ast.g:379:1: exprCol : ^( EXPRCOL IDENT ( IDENT )* ) ;
    public final void exprCol() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:380:2: ( ^( EXPRCOL IDENT ( IDENT )* ) )
            // EsperEPL2Ast.g:380:4: ^( EXPRCOL IDENT ( IDENT )* )
            {
            match(input,EXPRCOL,FOLLOW_EXPRCOL_in_exprCol2131); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_exprCol2133); 
            // EsperEPL2Ast.g:380:20: ( IDENT )*
            loop122:
            do {
                int alt122=2;
                int LA122_0 = input.LA(1);

                if ( (LA122_0==IDENT) ) {
                    alt122=1;
                }


                switch (alt122) {
            	case 1 :
            	    // EsperEPL2Ast.g:380:21: IDENT
            	    {
            	    match(input,IDENT,FOLLOW_IDENT_in_exprCol2136); 

            	    }
            	    break;

            	default :
            	    break loop122;
                }
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "exprCol"


    // $ANTLR start "selectClause"
    // EsperEPL2Ast.g:383:1: selectClause : ^(s= SELECTION_EXPR ( RSTREAM | ISTREAM | IRSTREAM )? ( DISTINCT )? selectionList ) ;
    public final void selectClause() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:384:2: ( ^(s= SELECTION_EXPR ( RSTREAM | ISTREAM | IRSTREAM )? ( DISTINCT )? selectionList ) )
            // EsperEPL2Ast.g:384:4: ^(s= SELECTION_EXPR ( RSTREAM | ISTREAM | IRSTREAM )? ( DISTINCT )? selectionList )
            {
            s=(CommonTree)match(input,SELECTION_EXPR,FOLLOW_SELECTION_EXPR_in_selectClause2154); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:384:23: ( RSTREAM | ISTREAM | IRSTREAM )?
            int alt123=2;
            int LA123_0 = input.LA(1);

            if ( ((LA123_0>=RSTREAM && LA123_0<=IRSTREAM)) ) {
                alt123=1;
            }
            switch (alt123) {
                case 1 :
                    // EsperEPL2Ast.g:
                    {
                    if ( (input.LA(1)>=RSTREAM && input.LA(1)<=IRSTREAM) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }

            // EsperEPL2Ast.g:384:55: ( DISTINCT )?
            int alt124=2;
            int LA124_0 = input.LA(1);

            if ( (LA124_0==DISTINCT) ) {
                alt124=1;
            }
            switch (alt124) {
                case 1 :
                    // EsperEPL2Ast.g:384:55: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_selectClause2169); 

                    }
                    break;

            }

            pushFollow(FOLLOW_selectionList_in_selectClause2172);
            selectionList();

            state._fsp--;

             leaveNode(s); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectClause"


    // $ANTLR start "fromClause"
    // EsperEPL2Ast.g:387:1: fromClause : streamExpression ( streamExpression ( outerJoin )* )* ;
    public final void fromClause() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:388:2: ( streamExpression ( streamExpression ( outerJoin )* )* )
            // EsperEPL2Ast.g:388:4: streamExpression ( streamExpression ( outerJoin )* )*
            {
            pushFollow(FOLLOW_streamExpression_in_fromClause2186);
            streamExpression();

            state._fsp--;

            // EsperEPL2Ast.g:388:21: ( streamExpression ( outerJoin )* )*
            loop126:
            do {
                int alt126=2;
                int LA126_0 = input.LA(1);

                if ( (LA126_0==STREAM_EXPR) ) {
                    alt126=1;
                }


                switch (alt126) {
            	case 1 :
            	    // EsperEPL2Ast.g:388:22: streamExpression ( outerJoin )*
            	    {
            	    pushFollow(FOLLOW_streamExpression_in_fromClause2189);
            	    streamExpression();

            	    state._fsp--;

            	    // EsperEPL2Ast.g:388:39: ( outerJoin )*
            	    loop125:
            	    do {
            	        int alt125=2;
            	        int LA125_0 = input.LA(1);

            	        if ( ((LA125_0>=INNERJOIN_EXPR && LA125_0<=FULL_OUTERJOIN_EXPR)) ) {
            	            alt125=1;
            	        }


            	        switch (alt125) {
            	    	case 1 :
            	    	    // EsperEPL2Ast.g:388:40: outerJoin
            	    	    {
            	    	    pushFollow(FOLLOW_outerJoin_in_fromClause2192);
            	    	    outerJoin();

            	    	    state._fsp--;


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop125;
            	        }
            	    } while (true);


            	    }
            	    break;

            	default :
            	    break loop126;
                }
            } while (true);


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "fromClause"


    // $ANTLR start "forExpr"
    // EsperEPL2Ast.g:391:1: forExpr : ^(f= FOR IDENT ( valueExpr )* ) ;
    public final void forExpr() throws RecognitionException {
        CommonTree f=null;

        try {
            // EsperEPL2Ast.g:392:2: ( ^(f= FOR IDENT ( valueExpr )* ) )
            // EsperEPL2Ast.g:392:4: ^(f= FOR IDENT ( valueExpr )* )
            {
            f=(CommonTree)match(input,FOR,FOLLOW_FOR_in_forExpr2212); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_forExpr2214); 
            // EsperEPL2Ast.g:392:18: ( valueExpr )*
            loop127:
            do {
                int alt127=2;
                int LA127_0 = input.LA(1);

                if ( ((LA127_0>=IN_SET && LA127_0<=REGEXP)||LA127_0==NOT_EXPR||(LA127_0>=SUM && LA127_0<=AVG)||(LA127_0>=COALESCE && LA127_0<=COUNT)||(LA127_0>=CASE && LA127_0<=CASE2)||LA127_0==ISTREAM||(LA127_0>=PREVIOUS && LA127_0<=EXISTS)||(LA127_0>=INSTANCEOF && LA127_0<=CURRENT_TIMESTAMP)||LA127_0==NEWKW||(LA127_0>=EVAL_AND_EXPR && LA127_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA127_0==EVENT_PROP_EXPR||LA127_0==CONCAT||(LA127_0>=LIB_FUNC_CHAIN && LA127_0<=DOT_EXPR)||LA127_0==ARRAY_EXPR||(LA127_0>=NOT_IN_SET && LA127_0<=NOT_REGEXP)||(LA127_0>=IN_RANGE && LA127_0<=SUBSELECT_EXPR)||(LA127_0>=EXISTS_SUBSELECT_EXPR && LA127_0<=NOT_IN_SUBSELECT_EXPR)||LA127_0==SUBSTITUTION||(LA127_0>=FIRST_AGGREG && LA127_0<=WINDOW_AGGREG)||(LA127_0>=INT_TYPE && LA127_0<=NULL_TYPE)||(LA127_0>=JSON_OBJECT && LA127_0<=JSON_ARRAY)||LA127_0==STAR||(LA127_0>=LT && LA127_0<=GT)||(LA127_0>=BOR && LA127_0<=PLUS)||(LA127_0>=BAND && LA127_0<=BXOR)||(LA127_0>=LE && LA127_0<=GE)||(LA127_0>=MINUS && LA127_0<=MOD)||(LA127_0>=EVAL_IS_GROUP_EXPR && LA127_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt127=1;
                }


                switch (alt127) {
            	case 1 :
            	    // EsperEPL2Ast.g:392:18: valueExpr
            	    {
            	    pushFollow(FOLLOW_valueExpr_in_forExpr2216);
            	    valueExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop127;
                }
            } while (true);

             leaveNode(f); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "forExpr"


    // $ANTLR start "matchRecogClause"
    // EsperEPL2Ast.g:395:1: matchRecogClause : ^(m= MATCH_RECOGNIZE ( matchRecogPartitionBy )? matchRecogMeasures ( ALL )? ( matchRecogMatchesAfterSkip )? matchRecogPattern ( matchRecogMatchesInterval )? matchRecogDefine ) ;
    public final void matchRecogClause() throws RecognitionException {
        CommonTree m=null;

        try {
            // EsperEPL2Ast.g:396:2: ( ^(m= MATCH_RECOGNIZE ( matchRecogPartitionBy )? matchRecogMeasures ( ALL )? ( matchRecogMatchesAfterSkip )? matchRecogPattern ( matchRecogMatchesInterval )? matchRecogDefine ) )
            // EsperEPL2Ast.g:396:4: ^(m= MATCH_RECOGNIZE ( matchRecogPartitionBy )? matchRecogMeasures ( ALL )? ( matchRecogMatchesAfterSkip )? matchRecogPattern ( matchRecogMatchesInterval )? matchRecogDefine )
            {
            m=(CommonTree)match(input,MATCH_RECOGNIZE,FOLLOW_MATCH_RECOGNIZE_in_matchRecogClause2235); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:396:24: ( matchRecogPartitionBy )?
            int alt128=2;
            int LA128_0 = input.LA(1);

            if ( (LA128_0==PARTITIONITEM) ) {
                alt128=1;
            }
            switch (alt128) {
                case 1 :
                    // EsperEPL2Ast.g:396:24: matchRecogPartitionBy
                    {
                    pushFollow(FOLLOW_matchRecogPartitionBy_in_matchRecogClause2237);
                    matchRecogPartitionBy();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_matchRecogMeasures_in_matchRecogClause2244);
            matchRecogMeasures();

            state._fsp--;

            // EsperEPL2Ast.g:398:4: ( ALL )?
            int alt129=2;
            int LA129_0 = input.LA(1);

            if ( (LA129_0==ALL) ) {
                alt129=1;
            }
            switch (alt129) {
                case 1 :
                    // EsperEPL2Ast.g:398:4: ALL
                    {
                    match(input,ALL,FOLLOW_ALL_in_matchRecogClause2250); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:399:4: ( matchRecogMatchesAfterSkip )?
            int alt130=2;
            int LA130_0 = input.LA(1);

            if ( (LA130_0==MATCHREC_AFTER_SKIP) ) {
                alt130=1;
            }
            switch (alt130) {
                case 1 :
                    // EsperEPL2Ast.g:399:4: matchRecogMatchesAfterSkip
                    {
                    pushFollow(FOLLOW_matchRecogMatchesAfterSkip_in_matchRecogClause2256);
                    matchRecogMatchesAfterSkip();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_matchRecogPattern_in_matchRecogClause2262);
            matchRecogPattern();

            state._fsp--;

            // EsperEPL2Ast.g:401:4: ( matchRecogMatchesInterval )?
            int alt131=2;
            int LA131_0 = input.LA(1);

            if ( (LA131_0==MATCHREC_INTERVAL) ) {
                alt131=1;
            }
            switch (alt131) {
                case 1 :
                    // EsperEPL2Ast.g:401:4: matchRecogMatchesInterval
                    {
                    pushFollow(FOLLOW_matchRecogMatchesInterval_in_matchRecogClause2268);
                    matchRecogMatchesInterval();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_matchRecogDefine_in_matchRecogClause2274);
            matchRecogDefine();

            state._fsp--;

             leaveNode(m); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogClause"


    // $ANTLR start "matchRecogPartitionBy"
    // EsperEPL2Ast.g:405:1: matchRecogPartitionBy : ^(p= PARTITIONITEM ( valueExpr )+ ) ;
    public final void matchRecogPartitionBy() throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:406:2: ( ^(p= PARTITIONITEM ( valueExpr )+ ) )
            // EsperEPL2Ast.g:406:4: ^(p= PARTITIONITEM ( valueExpr )+ )
            {
            p=(CommonTree)match(input,PARTITIONITEM,FOLLOW_PARTITIONITEM_in_matchRecogPartitionBy2292); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:406:22: ( valueExpr )+
            int cnt132=0;
            loop132:
            do {
                int alt132=2;
                int LA132_0 = input.LA(1);

                if ( ((LA132_0>=IN_SET && LA132_0<=REGEXP)||LA132_0==NOT_EXPR||(LA132_0>=SUM && LA132_0<=AVG)||(LA132_0>=COALESCE && LA132_0<=COUNT)||(LA132_0>=CASE && LA132_0<=CASE2)||LA132_0==ISTREAM||(LA132_0>=PREVIOUS && LA132_0<=EXISTS)||(LA132_0>=INSTANCEOF && LA132_0<=CURRENT_TIMESTAMP)||LA132_0==NEWKW||(LA132_0>=EVAL_AND_EXPR && LA132_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA132_0==EVENT_PROP_EXPR||LA132_0==CONCAT||(LA132_0>=LIB_FUNC_CHAIN && LA132_0<=DOT_EXPR)||LA132_0==ARRAY_EXPR||(LA132_0>=NOT_IN_SET && LA132_0<=NOT_REGEXP)||(LA132_0>=IN_RANGE && LA132_0<=SUBSELECT_EXPR)||(LA132_0>=EXISTS_SUBSELECT_EXPR && LA132_0<=NOT_IN_SUBSELECT_EXPR)||LA132_0==SUBSTITUTION||(LA132_0>=FIRST_AGGREG && LA132_0<=WINDOW_AGGREG)||(LA132_0>=INT_TYPE && LA132_0<=NULL_TYPE)||(LA132_0>=JSON_OBJECT && LA132_0<=JSON_ARRAY)||LA132_0==STAR||(LA132_0>=LT && LA132_0<=GT)||(LA132_0>=BOR && LA132_0<=PLUS)||(LA132_0>=BAND && LA132_0<=BXOR)||(LA132_0>=LE && LA132_0<=GE)||(LA132_0>=MINUS && LA132_0<=MOD)||(LA132_0>=EVAL_IS_GROUP_EXPR && LA132_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt132=1;
                }


                switch (alt132) {
            	case 1 :
            	    // EsperEPL2Ast.g:406:22: valueExpr
            	    {
            	    pushFollow(FOLLOW_valueExpr_in_matchRecogPartitionBy2294);
            	    valueExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt132 >= 1 ) break loop132;
                        EarlyExitException eee =
                            new EarlyExitException(132, input);
                        throw eee;
                }
                cnt132++;
            } while (true);

             leaveNode(p); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogPartitionBy"


    // $ANTLR start "matchRecogMatchesAfterSkip"
    // EsperEPL2Ast.g:409:1: matchRecogMatchesAfterSkip : ^( MATCHREC_AFTER_SKIP IDENT IDENT IDENT ( IDENT | LAST ) IDENT ) ;
    public final void matchRecogMatchesAfterSkip() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:410:2: ( ^( MATCHREC_AFTER_SKIP IDENT IDENT IDENT ( IDENT | LAST ) IDENT ) )
            // EsperEPL2Ast.g:410:4: ^( MATCHREC_AFTER_SKIP IDENT IDENT IDENT ( IDENT | LAST ) IDENT )
            {
            match(input,MATCHREC_AFTER_SKIP,FOLLOW_MATCHREC_AFTER_SKIP_in_matchRecogMatchesAfterSkip2311); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2313); 
            match(input,IDENT,FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2315); 
            match(input,IDENT,FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2317); 
            if ( input.LA(1)==LAST||input.LA(1)==IDENT ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            match(input,IDENT,FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2325); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogMatchesAfterSkip"


    // $ANTLR start "matchRecogMatchesInterval"
    // EsperEPL2Ast.g:413:1: matchRecogMatchesInterval : ^( MATCHREC_INTERVAL IDENT timePeriod ) ;
    public final void matchRecogMatchesInterval() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:414:2: ( ^( MATCHREC_INTERVAL IDENT timePeriod ) )
            // EsperEPL2Ast.g:414:4: ^( MATCHREC_INTERVAL IDENT timePeriod )
            {
            match(input,MATCHREC_INTERVAL,FOLLOW_MATCHREC_INTERVAL_in_matchRecogMatchesInterval2340); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_matchRecogMatchesInterval2342); 
            pushFollow(FOLLOW_timePeriod_in_matchRecogMatchesInterval2344);
            timePeriod();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogMatchesInterval"


    // $ANTLR start "matchRecogMeasures"
    // EsperEPL2Ast.g:417:1: matchRecogMeasures : ^(m= MATCHREC_MEASURES ( matchRecogMeasureListElement )* ) ;
    public final void matchRecogMeasures() throws RecognitionException {
        CommonTree m=null;

        try {
            // EsperEPL2Ast.g:418:2: ( ^(m= MATCHREC_MEASURES ( matchRecogMeasureListElement )* ) )
            // EsperEPL2Ast.g:418:4: ^(m= MATCHREC_MEASURES ( matchRecogMeasureListElement )* )
            {
            m=(CommonTree)match(input,MATCHREC_MEASURES,FOLLOW_MATCHREC_MEASURES_in_matchRecogMeasures2360); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:418:26: ( matchRecogMeasureListElement )*
                loop133:
                do {
                    int alt133=2;
                    int LA133_0 = input.LA(1);

                    if ( (LA133_0==MATCHREC_MEASURE_ITEM) ) {
                        alt133=1;
                    }


                    switch (alt133) {
                	case 1 :
                	    // EsperEPL2Ast.g:418:26: matchRecogMeasureListElement
                	    {
                	    pushFollow(FOLLOW_matchRecogMeasureListElement_in_matchRecogMeasures2362);
                	    matchRecogMeasureListElement();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop133;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogMeasures"


    // $ANTLR start "matchRecogMeasureListElement"
    // EsperEPL2Ast.g:421:1: matchRecogMeasureListElement : ^(m= MATCHREC_MEASURE_ITEM valueExpr ( IDENT )? ) ;
    public final void matchRecogMeasureListElement() throws RecognitionException {
        CommonTree m=null;

        try {
            // EsperEPL2Ast.g:422:2: ( ^(m= MATCHREC_MEASURE_ITEM valueExpr ( IDENT )? ) )
            // EsperEPL2Ast.g:422:4: ^(m= MATCHREC_MEASURE_ITEM valueExpr ( IDENT )? )
            {
            m=(CommonTree)match(input,MATCHREC_MEASURE_ITEM,FOLLOW_MATCHREC_MEASURE_ITEM_in_matchRecogMeasureListElement2379); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_matchRecogMeasureListElement2381);
            valueExpr();

            state._fsp--;

            // EsperEPL2Ast.g:422:40: ( IDENT )?
            int alt134=2;
            int LA134_0 = input.LA(1);

            if ( (LA134_0==IDENT) ) {
                alt134=1;
            }
            switch (alt134) {
                case 1 :
                    // EsperEPL2Ast.g:422:40: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_matchRecogMeasureListElement2383); 

                    }
                    break;

            }

             leaveNode(m); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogMeasureListElement"


    // $ANTLR start "matchRecogPattern"
    // EsperEPL2Ast.g:425:1: matchRecogPattern : ^(p= MATCHREC_PATTERN ( matchRecogPatternAlteration )+ ) ;
    public final void matchRecogPattern() throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:426:2: ( ^(p= MATCHREC_PATTERN ( matchRecogPatternAlteration )+ ) )
            // EsperEPL2Ast.g:426:4: ^(p= MATCHREC_PATTERN ( matchRecogPatternAlteration )+ )
            {
            p=(CommonTree)match(input,MATCHREC_PATTERN,FOLLOW_MATCHREC_PATTERN_in_matchRecogPattern2403); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:426:25: ( matchRecogPatternAlteration )+
            int cnt135=0;
            loop135:
            do {
                int alt135=2;
                int LA135_0 = input.LA(1);

                if ( ((LA135_0>=MATCHREC_PATTERN_CONCAT && LA135_0<=MATCHREC_PATTERN_ALTER)) ) {
                    alt135=1;
                }


                switch (alt135) {
            	case 1 :
            	    // EsperEPL2Ast.g:426:25: matchRecogPatternAlteration
            	    {
            	    pushFollow(FOLLOW_matchRecogPatternAlteration_in_matchRecogPattern2405);
            	    matchRecogPatternAlteration();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt135 >= 1 ) break loop135;
                        EarlyExitException eee =
                            new EarlyExitException(135, input);
                        throw eee;
                }
                cnt135++;
            } while (true);

             leaveNode(p); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogPattern"


    // $ANTLR start "matchRecogPatternAlteration"
    // EsperEPL2Ast.g:429:1: matchRecogPatternAlteration : ( matchRecogPatternConcat | ^(o= MATCHREC_PATTERN_ALTER matchRecogPatternConcat ( matchRecogPatternConcat )+ ) );
    public final void matchRecogPatternAlteration() throws RecognitionException {
        CommonTree o=null;

        try {
            // EsperEPL2Ast.g:430:2: ( matchRecogPatternConcat | ^(o= MATCHREC_PATTERN_ALTER matchRecogPatternConcat ( matchRecogPatternConcat )+ ) )
            int alt137=2;
            int LA137_0 = input.LA(1);

            if ( (LA137_0==MATCHREC_PATTERN_CONCAT) ) {
                alt137=1;
            }
            else if ( (LA137_0==MATCHREC_PATTERN_ALTER) ) {
                alt137=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 137, 0, input);

                throw nvae;
            }
            switch (alt137) {
                case 1 :
                    // EsperEPL2Ast.g:430:4: matchRecogPatternConcat
                    {
                    pushFollow(FOLLOW_matchRecogPatternConcat_in_matchRecogPatternAlteration2420);
                    matchRecogPatternConcat();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:431:4: ^(o= MATCHREC_PATTERN_ALTER matchRecogPatternConcat ( matchRecogPatternConcat )+ )
                    {
                    o=(CommonTree)match(input,MATCHREC_PATTERN_ALTER,FOLLOW_MATCHREC_PATTERN_ALTER_in_matchRecogPatternAlteration2428); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_matchRecogPatternConcat_in_matchRecogPatternAlteration2430);
                    matchRecogPatternConcat();

                    state._fsp--;

                    // EsperEPL2Ast.g:431:55: ( matchRecogPatternConcat )+
                    int cnt136=0;
                    loop136:
                    do {
                        int alt136=2;
                        int LA136_0 = input.LA(1);

                        if ( (LA136_0==MATCHREC_PATTERN_CONCAT) ) {
                            alt136=1;
                        }


                        switch (alt136) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:431:55: matchRecogPatternConcat
                    	    {
                    	    pushFollow(FOLLOW_matchRecogPatternConcat_in_matchRecogPatternAlteration2432);
                    	    matchRecogPatternConcat();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt136 >= 1 ) break loop136;
                                EarlyExitException eee =
                                    new EarlyExitException(136, input);
                                throw eee;
                        }
                        cnt136++;
                    } while (true);

                     leaveNode(o); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogPatternAlteration"


    // $ANTLR start "matchRecogPatternConcat"
    // EsperEPL2Ast.g:434:1: matchRecogPatternConcat : ^(p= MATCHREC_PATTERN_CONCAT ( matchRecogPatternUnary )+ ) ;
    public final void matchRecogPatternConcat() throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:435:2: ( ^(p= MATCHREC_PATTERN_CONCAT ( matchRecogPatternUnary )+ ) )
            // EsperEPL2Ast.g:435:4: ^(p= MATCHREC_PATTERN_CONCAT ( matchRecogPatternUnary )+ )
            {
            p=(CommonTree)match(input,MATCHREC_PATTERN_CONCAT,FOLLOW_MATCHREC_PATTERN_CONCAT_in_matchRecogPatternConcat2450); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:435:32: ( matchRecogPatternUnary )+
            int cnt138=0;
            loop138:
            do {
                int alt138=2;
                int LA138_0 = input.LA(1);

                if ( (LA138_0==MATCHREC_PATTERN_ATOM||LA138_0==MATCHREC_PATTERN_NESTED) ) {
                    alt138=1;
                }


                switch (alt138) {
            	case 1 :
            	    // EsperEPL2Ast.g:435:32: matchRecogPatternUnary
            	    {
            	    pushFollow(FOLLOW_matchRecogPatternUnary_in_matchRecogPatternConcat2452);
            	    matchRecogPatternUnary();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt138 >= 1 ) break loop138;
                        EarlyExitException eee =
                            new EarlyExitException(138, input);
                        throw eee;
                }
                cnt138++;
            } while (true);

             leaveNode(p); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogPatternConcat"


    // $ANTLR start "matchRecogPatternUnary"
    // EsperEPL2Ast.g:438:1: matchRecogPatternUnary : ( matchRecogPatternNested | matchRecogPatternAtom );
    public final void matchRecogPatternUnary() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:439:2: ( matchRecogPatternNested | matchRecogPatternAtom )
            int alt139=2;
            int LA139_0 = input.LA(1);

            if ( (LA139_0==MATCHREC_PATTERN_NESTED) ) {
                alt139=1;
            }
            else if ( (LA139_0==MATCHREC_PATTERN_ATOM) ) {
                alt139=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 139, 0, input);

                throw nvae;
            }
            switch (alt139) {
                case 1 :
                    // EsperEPL2Ast.g:439:4: matchRecogPatternNested
                    {
                    pushFollow(FOLLOW_matchRecogPatternNested_in_matchRecogPatternUnary2467);
                    matchRecogPatternNested();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:440:4: matchRecogPatternAtom
                    {
                    pushFollow(FOLLOW_matchRecogPatternAtom_in_matchRecogPatternUnary2472);
                    matchRecogPatternAtom();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogPatternUnary"


    // $ANTLR start "matchRecogPatternNested"
    // EsperEPL2Ast.g:443:1: matchRecogPatternNested : ^(p= MATCHREC_PATTERN_NESTED matchRecogPatternAlteration ( PLUS | STAR | QUESTION )? ) ;
    public final void matchRecogPatternNested() throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:444:2: ( ^(p= MATCHREC_PATTERN_NESTED matchRecogPatternAlteration ( PLUS | STAR | QUESTION )? ) )
            // EsperEPL2Ast.g:444:4: ^(p= MATCHREC_PATTERN_NESTED matchRecogPatternAlteration ( PLUS | STAR | QUESTION )? )
            {
            p=(CommonTree)match(input,MATCHREC_PATTERN_NESTED,FOLLOW_MATCHREC_PATTERN_NESTED_in_matchRecogPatternNested2487); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_matchRecogPatternAlteration_in_matchRecogPatternNested2489);
            matchRecogPatternAlteration();

            state._fsp--;

            // EsperEPL2Ast.g:444:60: ( PLUS | STAR | QUESTION )?
            int alt140=2;
            int LA140_0 = input.LA(1);

            if ( (LA140_0==STAR||LA140_0==QUESTION||LA140_0==PLUS) ) {
                alt140=1;
            }
            switch (alt140) {
                case 1 :
                    // EsperEPL2Ast.g:
                    {
                    if ( input.LA(1)==STAR||input.LA(1)==QUESTION||input.LA(1)==PLUS ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }

             leaveNode(p); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogPatternNested"


    // $ANTLR start "matchRecogPatternAtom"
    // EsperEPL2Ast.g:447:1: matchRecogPatternAtom : ^(p= MATCHREC_PATTERN_ATOM IDENT ( ( PLUS | STAR | QUESTION ) ( QUESTION )? )? ) ;
    public final void matchRecogPatternAtom() throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:448:2: ( ^(p= MATCHREC_PATTERN_ATOM IDENT ( ( PLUS | STAR | QUESTION ) ( QUESTION )? )? ) )
            // EsperEPL2Ast.g:448:4: ^(p= MATCHREC_PATTERN_ATOM IDENT ( ( PLUS | STAR | QUESTION ) ( QUESTION )? )? )
            {
            p=(CommonTree)match(input,MATCHREC_PATTERN_ATOM,FOLLOW_MATCHREC_PATTERN_ATOM_in_matchRecogPatternAtom2520); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_matchRecogPatternAtom2522); 
            // EsperEPL2Ast.g:448:36: ( ( PLUS | STAR | QUESTION ) ( QUESTION )? )?
            int alt142=2;
            int LA142_0 = input.LA(1);

            if ( (LA142_0==STAR||LA142_0==QUESTION||LA142_0==PLUS) ) {
                alt142=1;
            }
            switch (alt142) {
                case 1 :
                    // EsperEPL2Ast.g:448:38: ( PLUS | STAR | QUESTION ) ( QUESTION )?
                    {
                    if ( input.LA(1)==STAR||input.LA(1)==QUESTION||input.LA(1)==PLUS ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // EsperEPL2Ast.g:448:63: ( QUESTION )?
                    int alt141=2;
                    int LA141_0 = input.LA(1);

                    if ( (LA141_0==QUESTION) ) {
                        alt141=1;
                    }
                    switch (alt141) {
                        case 1 :
                            // EsperEPL2Ast.g:448:63: QUESTION
                            {
                            match(input,QUESTION,FOLLOW_QUESTION_in_matchRecogPatternAtom2538); 

                            }
                            break;

                    }


                    }
                    break;

            }

             leaveNode(p); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogPatternAtom"


    // $ANTLR start "matchRecogDefine"
    // EsperEPL2Ast.g:451:1: matchRecogDefine : ^(p= MATCHREC_DEFINE ( matchRecogDefineItem )+ ) ;
    public final void matchRecogDefine() throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:452:2: ( ^(p= MATCHREC_DEFINE ( matchRecogDefineItem )+ ) )
            // EsperEPL2Ast.g:452:4: ^(p= MATCHREC_DEFINE ( matchRecogDefineItem )+ )
            {
            p=(CommonTree)match(input,MATCHREC_DEFINE,FOLLOW_MATCHREC_DEFINE_in_matchRecogDefine2560); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:452:24: ( matchRecogDefineItem )+
            int cnt143=0;
            loop143:
            do {
                int alt143=2;
                int LA143_0 = input.LA(1);

                if ( (LA143_0==MATCHREC_DEFINE_ITEM) ) {
                    alt143=1;
                }


                switch (alt143) {
            	case 1 :
            	    // EsperEPL2Ast.g:452:24: matchRecogDefineItem
            	    {
            	    pushFollow(FOLLOW_matchRecogDefineItem_in_matchRecogDefine2562);
            	    matchRecogDefineItem();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt143 >= 1 ) break loop143;
                        EarlyExitException eee =
                            new EarlyExitException(143, input);
                        throw eee;
                }
                cnt143++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogDefine"


    // $ANTLR start "matchRecogDefineItem"
    // EsperEPL2Ast.g:455:1: matchRecogDefineItem : ^(d= MATCHREC_DEFINE_ITEM IDENT valueExpr ) ;
    public final void matchRecogDefineItem() throws RecognitionException {
        CommonTree d=null;

        try {
            // EsperEPL2Ast.g:456:2: ( ^(d= MATCHREC_DEFINE_ITEM IDENT valueExpr ) )
            // EsperEPL2Ast.g:456:4: ^(d= MATCHREC_DEFINE_ITEM IDENT valueExpr )
            {
            d=(CommonTree)match(input,MATCHREC_DEFINE_ITEM,FOLLOW_MATCHREC_DEFINE_ITEM_in_matchRecogDefineItem2579); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_matchRecogDefineItem2581); 
            pushFollow(FOLLOW_valueExpr_in_matchRecogDefineItem2583);
            valueExpr();

            state._fsp--;

             leaveNode(d); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchRecogDefineItem"


    // $ANTLR start "selectionList"
    // EsperEPL2Ast.g:460:1: selectionList : selectionListElement ( selectionListElement )* ;
    public final void selectionList() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:461:2: ( selectionListElement ( selectionListElement )* )
            // EsperEPL2Ast.g:461:4: selectionListElement ( selectionListElement )*
            {
            pushFollow(FOLLOW_selectionListElement_in_selectionList2600);
            selectionListElement();

            state._fsp--;

            // EsperEPL2Ast.g:461:25: ( selectionListElement )*
            loop144:
            do {
                int alt144=2;
                int LA144_0 = input.LA(1);

                if ( ((LA144_0>=SELECTION_ELEMENT_EXPR && LA144_0<=SELECTION_STREAM)||LA144_0==WILDCARD_SELECT) ) {
                    alt144=1;
                }


                switch (alt144) {
            	case 1 :
            	    // EsperEPL2Ast.g:461:26: selectionListElement
            	    {
            	    pushFollow(FOLLOW_selectionListElement_in_selectionList2603);
            	    selectionListElement();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop144;
                }
            } while (true);


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectionList"


    // $ANTLR start "selectionListElement"
    // EsperEPL2Ast.g:464:1: selectionListElement : (w= WILDCARD_SELECT | ^(e= SELECTION_ELEMENT_EXPR valueExpr ( IDENT )? ) | ^(s= SELECTION_STREAM IDENT ( IDENT )? ) );
    public final void selectionListElement() throws RecognitionException {
        CommonTree w=null;
        CommonTree e=null;
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:465:2: (w= WILDCARD_SELECT | ^(e= SELECTION_ELEMENT_EXPR valueExpr ( IDENT )? ) | ^(s= SELECTION_STREAM IDENT ( IDENT )? ) )
            int alt147=3;
            switch ( input.LA(1) ) {
            case WILDCARD_SELECT:
                {
                alt147=1;
                }
                break;
            case SELECTION_ELEMENT_EXPR:
                {
                alt147=2;
                }
                break;
            case SELECTION_STREAM:
                {
                alt147=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 147, 0, input);

                throw nvae;
            }

            switch (alt147) {
                case 1 :
                    // EsperEPL2Ast.g:465:4: w= WILDCARD_SELECT
                    {
                    w=(CommonTree)match(input,WILDCARD_SELECT,FOLLOW_WILDCARD_SELECT_in_selectionListElement2619); 
                     leaveNode(w); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:466:4: ^(e= SELECTION_ELEMENT_EXPR valueExpr ( IDENT )? )
                    {
                    e=(CommonTree)match(input,SELECTION_ELEMENT_EXPR,FOLLOW_SELECTION_ELEMENT_EXPR_in_selectionListElement2629); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_selectionListElement2631);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:466:41: ( IDENT )?
                    int alt145=2;
                    int LA145_0 = input.LA(1);

                    if ( (LA145_0==IDENT) ) {
                        alt145=1;
                    }
                    switch (alt145) {
                        case 1 :
                            // EsperEPL2Ast.g:466:42: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_selectionListElement2634); 

                            }
                            break;

                    }

                     leaveNode(e); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:467:4: ^(s= SELECTION_STREAM IDENT ( IDENT )? )
                    {
                    s=(CommonTree)match(input,SELECTION_STREAM,FOLLOW_SELECTION_STREAM_in_selectionListElement2648); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_selectionListElement2650); 
                    // EsperEPL2Ast.g:467:31: ( IDENT )?
                    int alt146=2;
                    int LA146_0 = input.LA(1);

                    if ( (LA146_0==IDENT) ) {
                        alt146=1;
                    }
                    switch (alt146) {
                        case 1 :
                            // EsperEPL2Ast.g:467:32: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_selectionListElement2653); 

                            }
                            break;

                    }

                     leaveNode(s); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectionListElement"


    // $ANTLR start "outerJoin"
    // EsperEPL2Ast.g:470:1: outerJoin : ( ^(tl= LEFT_OUTERJOIN_EXPR ON ( outerJoinIdent )? ) | ^(tr= RIGHT_OUTERJOIN_EXPR ON ( outerJoinIdent )? ) | ^(tf= FULL_OUTERJOIN_EXPR ON ( outerJoinIdent )? ) | ^(i= INNERJOIN_EXPR ON ( outerJoinIdent )? ) );
    public final void outerJoin() throws RecognitionException {
        CommonTree tl=null;
        CommonTree tr=null;
        CommonTree tf=null;
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:471:2: ( ^(tl= LEFT_OUTERJOIN_EXPR ON ( outerJoinIdent )? ) | ^(tr= RIGHT_OUTERJOIN_EXPR ON ( outerJoinIdent )? ) | ^(tf= FULL_OUTERJOIN_EXPR ON ( outerJoinIdent )? ) | ^(i= INNERJOIN_EXPR ON ( outerJoinIdent )? ) )
            int alt152=4;
            switch ( input.LA(1) ) {
            case LEFT_OUTERJOIN_EXPR:
                {
                alt152=1;
                }
                break;
            case RIGHT_OUTERJOIN_EXPR:
                {
                alt152=2;
                }
                break;
            case FULL_OUTERJOIN_EXPR:
                {
                alt152=3;
                }
                break;
            case INNERJOIN_EXPR:
                {
                alt152=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 152, 0, input);

                throw nvae;
            }

            switch (alt152) {
                case 1 :
                    // EsperEPL2Ast.g:471:4: ^(tl= LEFT_OUTERJOIN_EXPR ON ( outerJoinIdent )? )
                    {
                    tl=(CommonTree)match(input,LEFT_OUTERJOIN_EXPR,FOLLOW_LEFT_OUTERJOIN_EXPR_in_outerJoin2675); 

                    match(input, Token.DOWN, null); 
                    match(input,ON,FOLLOW_ON_in_outerJoin2677); 
                    // EsperEPL2Ast.g:471:32: ( outerJoinIdent )?
                    int alt148=2;
                    int LA148_0 = input.LA(1);

                    if ( (LA148_0==EVENT_PROP_EXPR) ) {
                        alt148=1;
                    }
                    switch (alt148) {
                        case 1 :
                            // EsperEPL2Ast.g:471:32: outerJoinIdent
                            {
                            pushFollow(FOLLOW_outerJoinIdent_in_outerJoin2679);
                            outerJoinIdent();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(tl); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:472:4: ^(tr= RIGHT_OUTERJOIN_EXPR ON ( outerJoinIdent )? )
                    {
                    tr=(CommonTree)match(input,RIGHT_OUTERJOIN_EXPR,FOLLOW_RIGHT_OUTERJOIN_EXPR_in_outerJoin2692); 

                    match(input, Token.DOWN, null); 
                    match(input,ON,FOLLOW_ON_in_outerJoin2694); 
                    // EsperEPL2Ast.g:472:33: ( outerJoinIdent )?
                    int alt149=2;
                    int LA149_0 = input.LA(1);

                    if ( (LA149_0==EVENT_PROP_EXPR) ) {
                        alt149=1;
                    }
                    switch (alt149) {
                        case 1 :
                            // EsperEPL2Ast.g:472:33: outerJoinIdent
                            {
                            pushFollow(FOLLOW_outerJoinIdent_in_outerJoin2696);
                            outerJoinIdent();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(tr); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:473:4: ^(tf= FULL_OUTERJOIN_EXPR ON ( outerJoinIdent )? )
                    {
                    tf=(CommonTree)match(input,FULL_OUTERJOIN_EXPR,FOLLOW_FULL_OUTERJOIN_EXPR_in_outerJoin2709); 

                    match(input, Token.DOWN, null); 
                    match(input,ON,FOLLOW_ON_in_outerJoin2711); 
                    // EsperEPL2Ast.g:473:32: ( outerJoinIdent )?
                    int alt150=2;
                    int LA150_0 = input.LA(1);

                    if ( (LA150_0==EVENT_PROP_EXPR) ) {
                        alt150=1;
                    }
                    switch (alt150) {
                        case 1 :
                            // EsperEPL2Ast.g:473:32: outerJoinIdent
                            {
                            pushFollow(FOLLOW_outerJoinIdent_in_outerJoin2713);
                            outerJoinIdent();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(tf); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:474:4: ^(i= INNERJOIN_EXPR ON ( outerJoinIdent )? )
                    {
                    i=(CommonTree)match(input,INNERJOIN_EXPR,FOLLOW_INNERJOIN_EXPR_in_outerJoin2726); 

                    match(input, Token.DOWN, null); 
                    match(input,ON,FOLLOW_ON_in_outerJoin2728); 
                    // EsperEPL2Ast.g:474:26: ( outerJoinIdent )?
                    int alt151=2;
                    int LA151_0 = input.LA(1);

                    if ( (LA151_0==EVENT_PROP_EXPR) ) {
                        alt151=1;
                    }
                    switch (alt151) {
                        case 1 :
                            // EsperEPL2Ast.g:474:26: outerJoinIdent
                            {
                            pushFollow(FOLLOW_outerJoinIdent_in_outerJoin2730);
                            outerJoinIdent();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(i); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "outerJoin"


    // $ANTLR start "outerJoinIdent"
    // EsperEPL2Ast.g:477:1: outerJoinIdent : eventPropertyExpr[true] eventPropertyExpr[true] ( eventPropertyExpr[true] eventPropertyExpr[true] )* ;
    public final void outerJoinIdent() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:478:2: ( eventPropertyExpr[true] eventPropertyExpr[true] ( eventPropertyExpr[true] eventPropertyExpr[true] )* )
            // EsperEPL2Ast.g:478:4: eventPropertyExpr[true] eventPropertyExpr[true] ( eventPropertyExpr[true] eventPropertyExpr[true] )*
            {
            pushFollow(FOLLOW_eventPropertyExpr_in_outerJoinIdent2746);
            eventPropertyExpr(true);

            state._fsp--;

            pushFollow(FOLLOW_eventPropertyExpr_in_outerJoinIdent2749);
            eventPropertyExpr(true);

            state._fsp--;

            // EsperEPL2Ast.g:478:52: ( eventPropertyExpr[true] eventPropertyExpr[true] )*
            loop153:
            do {
                int alt153=2;
                int LA153_0 = input.LA(1);

                if ( (LA153_0==EVENT_PROP_EXPR) ) {
                    alt153=1;
                }


                switch (alt153) {
            	case 1 :
            	    // EsperEPL2Ast.g:478:53: eventPropertyExpr[true] eventPropertyExpr[true]
            	    {
            	    pushFollow(FOLLOW_eventPropertyExpr_in_outerJoinIdent2753);
            	    eventPropertyExpr(true);

            	    state._fsp--;

            	    pushFollow(FOLLOW_eventPropertyExpr_in_outerJoinIdent2756);
            	    eventPropertyExpr(true);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop153;
                }
            } while (true);


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "outerJoinIdent"


    // $ANTLR start "streamExpression"
    // EsperEPL2Ast.g:481:1: streamExpression : ^(v= STREAM_EXPR ( eventFilterExpr[true] | patternInclusionExpression | databaseJoinExpression | methodJoinExpression ) ( viewListExpr )? ( IDENT )? ( UNIDIRECTIONAL )? ( RETAINUNION | RETAININTERSECTION )? ) ;
    public final void streamExpression() throws RecognitionException {
        CommonTree v=null;

        try {
            // EsperEPL2Ast.g:482:2: ( ^(v= STREAM_EXPR ( eventFilterExpr[true] | patternInclusionExpression | databaseJoinExpression | methodJoinExpression ) ( viewListExpr )? ( IDENT )? ( UNIDIRECTIONAL )? ( RETAINUNION | RETAININTERSECTION )? ) )
            // EsperEPL2Ast.g:482:4: ^(v= STREAM_EXPR ( eventFilterExpr[true] | patternInclusionExpression | databaseJoinExpression | methodJoinExpression ) ( viewListExpr )? ( IDENT )? ( UNIDIRECTIONAL )? ( RETAINUNION | RETAININTERSECTION )? )
            {
            v=(CommonTree)match(input,STREAM_EXPR,FOLLOW_STREAM_EXPR_in_streamExpression2773); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:482:20: ( eventFilterExpr[true] | patternInclusionExpression | databaseJoinExpression | methodJoinExpression )
            int alt154=4;
            switch ( input.LA(1) ) {
            case EVENT_FILTER_EXPR:
                {
                alt154=1;
                }
                break;
            case PATTERN_INCL_EXPR:
                {
                alt154=2;
                }
                break;
            case DATABASE_JOIN_EXPR:
                {
                alt154=3;
                }
                break;
            case METHOD_JOIN_EXPR:
                {
                alt154=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 154, 0, input);

                throw nvae;
            }

            switch (alt154) {
                case 1 :
                    // EsperEPL2Ast.g:482:21: eventFilterExpr[true]
                    {
                    pushFollow(FOLLOW_eventFilterExpr_in_streamExpression2776);
                    eventFilterExpr(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:482:45: patternInclusionExpression
                    {
                    pushFollow(FOLLOW_patternInclusionExpression_in_streamExpression2781);
                    patternInclusionExpression();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:482:74: databaseJoinExpression
                    {
                    pushFollow(FOLLOW_databaseJoinExpression_in_streamExpression2785);
                    databaseJoinExpression();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:482:99: methodJoinExpression
                    {
                    pushFollow(FOLLOW_methodJoinExpression_in_streamExpression2789);
                    methodJoinExpression();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:482:121: ( viewListExpr )?
            int alt155=2;
            int LA155_0 = input.LA(1);

            if ( (LA155_0==VIEW_EXPR) ) {
                alt155=1;
            }
            switch (alt155) {
                case 1 :
                    // EsperEPL2Ast.g:482:122: viewListExpr
                    {
                    pushFollow(FOLLOW_viewListExpr_in_streamExpression2793);
                    viewListExpr();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:482:137: ( IDENT )?
            int alt156=2;
            int LA156_0 = input.LA(1);

            if ( (LA156_0==IDENT) ) {
                alt156=1;
            }
            switch (alt156) {
                case 1 :
                    // EsperEPL2Ast.g:482:138: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_streamExpression2798); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:482:146: ( UNIDIRECTIONAL )?
            int alt157=2;
            int LA157_0 = input.LA(1);

            if ( (LA157_0==UNIDIRECTIONAL) ) {
                alt157=1;
            }
            switch (alt157) {
                case 1 :
                    // EsperEPL2Ast.g:482:147: UNIDIRECTIONAL
                    {
                    match(input,UNIDIRECTIONAL,FOLLOW_UNIDIRECTIONAL_in_streamExpression2803); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:482:164: ( RETAINUNION | RETAININTERSECTION )?
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( ((LA158_0>=RETAINUNION && LA158_0<=RETAININTERSECTION)) ) {
                alt158=1;
            }
            switch (alt158) {
                case 1 :
                    // EsperEPL2Ast.g:
                    {
                    if ( (input.LA(1)>=RETAINUNION && input.LA(1)<=RETAININTERSECTION) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }

             leaveNode(v); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "streamExpression"


    // $ANTLR start "eventFilterExpr"
    // EsperEPL2Ast.g:485:1: eventFilterExpr[boolean isLeaveNode] : ^(f= EVENT_FILTER_EXPR ( IDENT )? CLASS_IDENT ( propertyExpression )? ( valueExpr )* ) ;
    public final void eventFilterExpr(boolean isLeaveNode) throws RecognitionException {
        CommonTree f=null;

        try {
            // EsperEPL2Ast.g:486:2: ( ^(f= EVENT_FILTER_EXPR ( IDENT )? CLASS_IDENT ( propertyExpression )? ( valueExpr )* ) )
            // EsperEPL2Ast.g:486:4: ^(f= EVENT_FILTER_EXPR ( IDENT )? CLASS_IDENT ( propertyExpression )? ( valueExpr )* )
            {
            f=(CommonTree)match(input,EVENT_FILTER_EXPR,FOLLOW_EVENT_FILTER_EXPR_in_eventFilterExpr2832); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:486:27: ( IDENT )?
            int alt159=2;
            int LA159_0 = input.LA(1);

            if ( (LA159_0==IDENT) ) {
                alt159=1;
            }
            switch (alt159) {
                case 1 :
                    // EsperEPL2Ast.g:486:27: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_eventFilterExpr2834); 

                    }
                    break;

            }

            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_eventFilterExpr2837); 
            // EsperEPL2Ast.g:486:46: ( propertyExpression )?
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==EVENT_FILTER_PROPERTY_EXPR) ) {
                alt160=1;
            }
            switch (alt160) {
                case 1 :
                    // EsperEPL2Ast.g:486:46: propertyExpression
                    {
                    pushFollow(FOLLOW_propertyExpression_in_eventFilterExpr2839);
                    propertyExpression();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:486:66: ( valueExpr )*
            loop161:
            do {
                int alt161=2;
                int LA161_0 = input.LA(1);

                if ( ((LA161_0>=IN_SET && LA161_0<=REGEXP)||LA161_0==NOT_EXPR||(LA161_0>=SUM && LA161_0<=AVG)||(LA161_0>=COALESCE && LA161_0<=COUNT)||(LA161_0>=CASE && LA161_0<=CASE2)||LA161_0==ISTREAM||(LA161_0>=PREVIOUS && LA161_0<=EXISTS)||(LA161_0>=INSTANCEOF && LA161_0<=CURRENT_TIMESTAMP)||LA161_0==NEWKW||(LA161_0>=EVAL_AND_EXPR && LA161_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA161_0==EVENT_PROP_EXPR||LA161_0==CONCAT||(LA161_0>=LIB_FUNC_CHAIN && LA161_0<=DOT_EXPR)||LA161_0==ARRAY_EXPR||(LA161_0>=NOT_IN_SET && LA161_0<=NOT_REGEXP)||(LA161_0>=IN_RANGE && LA161_0<=SUBSELECT_EXPR)||(LA161_0>=EXISTS_SUBSELECT_EXPR && LA161_0<=NOT_IN_SUBSELECT_EXPR)||LA161_0==SUBSTITUTION||(LA161_0>=FIRST_AGGREG && LA161_0<=WINDOW_AGGREG)||(LA161_0>=INT_TYPE && LA161_0<=NULL_TYPE)||(LA161_0>=JSON_OBJECT && LA161_0<=JSON_ARRAY)||LA161_0==STAR||(LA161_0>=LT && LA161_0<=GT)||(LA161_0>=BOR && LA161_0<=PLUS)||(LA161_0>=BAND && LA161_0<=BXOR)||(LA161_0>=LE && LA161_0<=GE)||(LA161_0>=MINUS && LA161_0<=MOD)||(LA161_0>=EVAL_IS_GROUP_EXPR && LA161_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt161=1;
                }


                switch (alt161) {
            	case 1 :
            	    // EsperEPL2Ast.g:486:67: valueExpr
            	    {
            	    pushFollow(FOLLOW_valueExpr_in_eventFilterExpr2843);
            	    valueExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop161;
                }
            } while (true);

             if (isLeaveNode) leaveNode(f); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "eventFilterExpr"


    // $ANTLR start "propertyExpression"
    // EsperEPL2Ast.g:489:1: propertyExpression : ^( EVENT_FILTER_PROPERTY_EXPR ( propertyExpressionAtom )* ) ;
    public final void propertyExpression() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:490:2: ( ^( EVENT_FILTER_PROPERTY_EXPR ( propertyExpressionAtom )* ) )
            // EsperEPL2Ast.g:490:4: ^( EVENT_FILTER_PROPERTY_EXPR ( propertyExpressionAtom )* )
            {
            match(input,EVENT_FILTER_PROPERTY_EXPR,FOLLOW_EVENT_FILTER_PROPERTY_EXPR_in_propertyExpression2863); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:490:34: ( propertyExpressionAtom )*
                loop162:
                do {
                    int alt162=2;
                    int LA162_0 = input.LA(1);

                    if ( (LA162_0==EVENT_FILTER_PROPERTY_EXPR_ATOM) ) {
                        alt162=1;
                    }


                    switch (alt162) {
                	case 1 :
                	    // EsperEPL2Ast.g:490:34: propertyExpressionAtom
                	    {
                	    pushFollow(FOLLOW_propertyExpressionAtom_in_propertyExpression2865);
                	    propertyExpressionAtom();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop162;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "propertyExpression"


    // $ANTLR start "propertyExpressionAtom"
    // EsperEPL2Ast.g:493:1: propertyExpressionAtom : ^(a= EVENT_FILTER_PROPERTY_EXPR_ATOM ( ^( SELECT ( propertySelectionListElement )+ ) )? valueExpr ( ^( ATCHAR IDENT IDENT ) )? ( IDENT )? ^( WHERE_EXPR ( valueExpr )? ) ) ;
    public final void propertyExpressionAtom() throws RecognitionException {
        CommonTree a=null;

        try {
            // EsperEPL2Ast.g:494:2: ( ^(a= EVENT_FILTER_PROPERTY_EXPR_ATOM ( ^( SELECT ( propertySelectionListElement )+ ) )? valueExpr ( ^( ATCHAR IDENT IDENT ) )? ( IDENT )? ^( WHERE_EXPR ( valueExpr )? ) ) )
            // EsperEPL2Ast.g:494:4: ^(a= EVENT_FILTER_PROPERTY_EXPR_ATOM ( ^( SELECT ( propertySelectionListElement )+ ) )? valueExpr ( ^( ATCHAR IDENT IDENT ) )? ( IDENT )? ^( WHERE_EXPR ( valueExpr )? ) )
            {
            a=(CommonTree)match(input,EVENT_FILTER_PROPERTY_EXPR_ATOM,FOLLOW_EVENT_FILTER_PROPERTY_EXPR_ATOM_in_propertyExpressionAtom2884); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:494:41: ( ^( SELECT ( propertySelectionListElement )+ ) )?
            int alt164=2;
            int LA164_0 = input.LA(1);

            if ( (LA164_0==SELECT) ) {
                alt164=1;
            }
            switch (alt164) {
                case 1 :
                    // EsperEPL2Ast.g:494:42: ^( SELECT ( propertySelectionListElement )+ )
                    {
                    match(input,SELECT,FOLLOW_SELECT_in_propertyExpressionAtom2888); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:494:51: ( propertySelectionListElement )+
                    int cnt163=0;
                    loop163:
                    do {
                        int alt163=2;
                        int LA163_0 = input.LA(1);

                        if ( ((LA163_0>=PROPERTY_SELECTION_ELEMENT_EXPR && LA163_0<=PROPERTY_WILDCARD_SELECT)) ) {
                            alt163=1;
                        }


                        switch (alt163) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:494:51: propertySelectionListElement
                    	    {
                    	    pushFollow(FOLLOW_propertySelectionListElement_in_propertyExpressionAtom2890);
                    	    propertySelectionListElement();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt163 >= 1 ) break loop163;
                                EarlyExitException eee =
                                    new EarlyExitException(163, input);
                                throw eee;
                        }
                        cnt163++;
                    } while (true);


                    match(input, Token.UP, null); 

                    }
                    break;

            }

            pushFollow(FOLLOW_valueExpr_in_propertyExpressionAtom2896);
            valueExpr();

            state._fsp--;

            // EsperEPL2Ast.g:494:94: ( ^( ATCHAR IDENT IDENT ) )?
            int alt165=2;
            int LA165_0 = input.LA(1);

            if ( (LA165_0==ATCHAR) ) {
                alt165=1;
            }
            switch (alt165) {
                case 1 :
                    // EsperEPL2Ast.g:494:95: ^( ATCHAR IDENT IDENT )
                    {
                    match(input,ATCHAR,FOLLOW_ATCHAR_in_propertyExpressionAtom2900); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_propertyExpressionAtom2902); 
                    match(input,IDENT,FOLLOW_IDENT_in_propertyExpressionAtom2904); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:494:119: ( IDENT )?
            int alt166=2;
            int LA166_0 = input.LA(1);

            if ( (LA166_0==IDENT) ) {
                alt166=1;
            }
            switch (alt166) {
                case 1 :
                    // EsperEPL2Ast.g:494:119: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_propertyExpressionAtom2909); 

                    }
                    break;

            }

            match(input,WHERE_EXPR,FOLLOW_WHERE_EXPR_in_propertyExpressionAtom2913); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:494:139: ( valueExpr )?
                int alt167=2;
                int LA167_0 = input.LA(1);

                if ( ((LA167_0>=IN_SET && LA167_0<=REGEXP)||LA167_0==NOT_EXPR||(LA167_0>=SUM && LA167_0<=AVG)||(LA167_0>=COALESCE && LA167_0<=COUNT)||(LA167_0>=CASE && LA167_0<=CASE2)||LA167_0==ISTREAM||(LA167_0>=PREVIOUS && LA167_0<=EXISTS)||(LA167_0>=INSTANCEOF && LA167_0<=CURRENT_TIMESTAMP)||LA167_0==NEWKW||(LA167_0>=EVAL_AND_EXPR && LA167_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA167_0==EVENT_PROP_EXPR||LA167_0==CONCAT||(LA167_0>=LIB_FUNC_CHAIN && LA167_0<=DOT_EXPR)||LA167_0==ARRAY_EXPR||(LA167_0>=NOT_IN_SET && LA167_0<=NOT_REGEXP)||(LA167_0>=IN_RANGE && LA167_0<=SUBSELECT_EXPR)||(LA167_0>=EXISTS_SUBSELECT_EXPR && LA167_0<=NOT_IN_SUBSELECT_EXPR)||LA167_0==SUBSTITUTION||(LA167_0>=FIRST_AGGREG && LA167_0<=WINDOW_AGGREG)||(LA167_0>=INT_TYPE && LA167_0<=NULL_TYPE)||(LA167_0>=JSON_OBJECT && LA167_0<=JSON_ARRAY)||LA167_0==STAR||(LA167_0>=LT && LA167_0<=GT)||(LA167_0>=BOR && LA167_0<=PLUS)||(LA167_0>=BAND && LA167_0<=BXOR)||(LA167_0>=LE && LA167_0<=GE)||(LA167_0>=MINUS && LA167_0<=MOD)||(LA167_0>=EVAL_IS_GROUP_EXPR && LA167_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt167=1;
                }
                switch (alt167) {
                    case 1 :
                        // EsperEPL2Ast.g:494:139: valueExpr
                        {
                        pushFollow(FOLLOW_valueExpr_in_propertyExpressionAtom2915);
                        valueExpr();

                        state._fsp--;


                        }
                        break;

                }


                match(input, Token.UP, null); 
            }
             leaveNode(a); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "propertyExpressionAtom"


    // $ANTLR start "propertySelectionListElement"
    // EsperEPL2Ast.g:497:1: propertySelectionListElement : (w= PROPERTY_WILDCARD_SELECT | ^(e= PROPERTY_SELECTION_ELEMENT_EXPR valueExpr ( IDENT )? ) | ^(s= PROPERTY_SELECTION_STREAM IDENT ( IDENT )? ) );
    public final void propertySelectionListElement() throws RecognitionException {
        CommonTree w=null;
        CommonTree e=null;
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:498:2: (w= PROPERTY_WILDCARD_SELECT | ^(e= PROPERTY_SELECTION_ELEMENT_EXPR valueExpr ( IDENT )? ) | ^(s= PROPERTY_SELECTION_STREAM IDENT ( IDENT )? ) )
            int alt170=3;
            switch ( input.LA(1) ) {
            case PROPERTY_WILDCARD_SELECT:
                {
                alt170=1;
                }
                break;
            case PROPERTY_SELECTION_ELEMENT_EXPR:
                {
                alt170=2;
                }
                break;
            case PROPERTY_SELECTION_STREAM:
                {
                alt170=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 170, 0, input);

                throw nvae;
            }

            switch (alt170) {
                case 1 :
                    // EsperEPL2Ast.g:498:4: w= PROPERTY_WILDCARD_SELECT
                    {
                    w=(CommonTree)match(input,PROPERTY_WILDCARD_SELECT,FOLLOW_PROPERTY_WILDCARD_SELECT_in_propertySelectionListElement2935); 
                     leaveNode(w); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:499:4: ^(e= PROPERTY_SELECTION_ELEMENT_EXPR valueExpr ( IDENT )? )
                    {
                    e=(CommonTree)match(input,PROPERTY_SELECTION_ELEMENT_EXPR,FOLLOW_PROPERTY_SELECTION_ELEMENT_EXPR_in_propertySelectionListElement2945); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_propertySelectionListElement2947);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:499:50: ( IDENT )?
                    int alt168=2;
                    int LA168_0 = input.LA(1);

                    if ( (LA168_0==IDENT) ) {
                        alt168=1;
                    }
                    switch (alt168) {
                        case 1 :
                            // EsperEPL2Ast.g:499:51: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_propertySelectionListElement2950); 

                            }
                            break;

                    }

                     leaveNode(e); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:500:4: ^(s= PROPERTY_SELECTION_STREAM IDENT ( IDENT )? )
                    {
                    s=(CommonTree)match(input,PROPERTY_SELECTION_STREAM,FOLLOW_PROPERTY_SELECTION_STREAM_in_propertySelectionListElement2964); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_propertySelectionListElement2966); 
                    // EsperEPL2Ast.g:500:40: ( IDENT )?
                    int alt169=2;
                    int LA169_0 = input.LA(1);

                    if ( (LA169_0==IDENT) ) {
                        alt169=1;
                    }
                    switch (alt169) {
                        case 1 :
                            // EsperEPL2Ast.g:500:41: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_propertySelectionListElement2969); 

                            }
                            break;

                    }

                     leaveNode(s); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "propertySelectionListElement"


    // $ANTLR start "patternInclusionExpression"
    // EsperEPL2Ast.g:503:1: patternInclusionExpression : ^(p= PATTERN_INCL_EXPR exprChoice ) ;
    public final void patternInclusionExpression() throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:504:2: ( ^(p= PATTERN_INCL_EXPR exprChoice ) )
            // EsperEPL2Ast.g:504:4: ^(p= PATTERN_INCL_EXPR exprChoice )
            {
            p=(CommonTree)match(input,PATTERN_INCL_EXPR,FOLLOW_PATTERN_INCL_EXPR_in_patternInclusionExpression2990); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_exprChoice_in_patternInclusionExpression2992);
            exprChoice();

            state._fsp--;

             leaveNode(p); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "patternInclusionExpression"


    // $ANTLR start "databaseJoinExpression"
    // EsperEPL2Ast.g:507:1: databaseJoinExpression : ^( DATABASE_JOIN_EXPR IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) ( STRING_LITERAL | QUOTED_STRING_LITERAL )? ) ;
    public final void databaseJoinExpression() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:508:2: ( ^( DATABASE_JOIN_EXPR IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) ( STRING_LITERAL | QUOTED_STRING_LITERAL )? ) )
            // EsperEPL2Ast.g:508:4: ^( DATABASE_JOIN_EXPR IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) ( STRING_LITERAL | QUOTED_STRING_LITERAL )? )
            {
            match(input,DATABASE_JOIN_EXPR,FOLLOW_DATABASE_JOIN_EXPR_in_databaseJoinExpression3009); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_databaseJoinExpression3011); 
            if ( (input.LA(1)>=STRING_LITERAL && input.LA(1)<=QUOTED_STRING_LITERAL) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            // EsperEPL2Ast.g:508:72: ( STRING_LITERAL | QUOTED_STRING_LITERAL )?
            int alt171=2;
            int LA171_0 = input.LA(1);

            if ( ((LA171_0>=STRING_LITERAL && LA171_0<=QUOTED_STRING_LITERAL)) ) {
                alt171=1;
            }
            switch (alt171) {
                case 1 :
                    // EsperEPL2Ast.g:
                    {
                    if ( (input.LA(1)>=STRING_LITERAL && input.LA(1)<=QUOTED_STRING_LITERAL) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "databaseJoinExpression"


    // $ANTLR start "methodJoinExpression"
    // EsperEPL2Ast.g:511:1: methodJoinExpression : ^( METHOD_JOIN_EXPR IDENT CLASS_IDENT ( valueExpr )* ) ;
    public final void methodJoinExpression() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:512:2: ( ^( METHOD_JOIN_EXPR IDENT CLASS_IDENT ( valueExpr )* ) )
            // EsperEPL2Ast.g:512:4: ^( METHOD_JOIN_EXPR IDENT CLASS_IDENT ( valueExpr )* )
            {
            match(input,METHOD_JOIN_EXPR,FOLLOW_METHOD_JOIN_EXPR_in_methodJoinExpression3042); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_methodJoinExpression3044); 
            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_methodJoinExpression3046); 
            // EsperEPL2Ast.g:512:41: ( valueExpr )*
            loop172:
            do {
                int alt172=2;
                int LA172_0 = input.LA(1);

                if ( ((LA172_0>=IN_SET && LA172_0<=REGEXP)||LA172_0==NOT_EXPR||(LA172_0>=SUM && LA172_0<=AVG)||(LA172_0>=COALESCE && LA172_0<=COUNT)||(LA172_0>=CASE && LA172_0<=CASE2)||LA172_0==ISTREAM||(LA172_0>=PREVIOUS && LA172_0<=EXISTS)||(LA172_0>=INSTANCEOF && LA172_0<=CURRENT_TIMESTAMP)||LA172_0==NEWKW||(LA172_0>=EVAL_AND_EXPR && LA172_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA172_0==EVENT_PROP_EXPR||LA172_0==CONCAT||(LA172_0>=LIB_FUNC_CHAIN && LA172_0<=DOT_EXPR)||LA172_0==ARRAY_EXPR||(LA172_0>=NOT_IN_SET && LA172_0<=NOT_REGEXP)||(LA172_0>=IN_RANGE && LA172_0<=SUBSELECT_EXPR)||(LA172_0>=EXISTS_SUBSELECT_EXPR && LA172_0<=NOT_IN_SUBSELECT_EXPR)||LA172_0==SUBSTITUTION||(LA172_0>=FIRST_AGGREG && LA172_0<=WINDOW_AGGREG)||(LA172_0>=INT_TYPE && LA172_0<=NULL_TYPE)||(LA172_0>=JSON_OBJECT && LA172_0<=JSON_ARRAY)||LA172_0==STAR||(LA172_0>=LT && LA172_0<=GT)||(LA172_0>=BOR && LA172_0<=PLUS)||(LA172_0>=BAND && LA172_0<=BXOR)||(LA172_0>=LE && LA172_0<=GE)||(LA172_0>=MINUS && LA172_0<=MOD)||(LA172_0>=EVAL_IS_GROUP_EXPR && LA172_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt172=1;
                }


                switch (alt172) {
            	case 1 :
            	    // EsperEPL2Ast.g:512:42: valueExpr
            	    {
            	    pushFollow(FOLLOW_valueExpr_in_methodJoinExpression3049);
            	    valueExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop172;
                }
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "methodJoinExpression"


    // $ANTLR start "viewListExpr"
    // EsperEPL2Ast.g:515:1: viewListExpr : viewExpr ( viewExpr )* ;
    public final void viewListExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:516:2: ( viewExpr ( viewExpr )* )
            // EsperEPL2Ast.g:516:4: viewExpr ( viewExpr )*
            {
            pushFollow(FOLLOW_viewExpr_in_viewListExpr3063);
            viewExpr();

            state._fsp--;

            // EsperEPL2Ast.g:516:13: ( viewExpr )*
            loop173:
            do {
                int alt173=2;
                int LA173_0 = input.LA(1);

                if ( (LA173_0==VIEW_EXPR) ) {
                    alt173=1;
                }


                switch (alt173) {
            	case 1 :
            	    // EsperEPL2Ast.g:516:14: viewExpr
            	    {
            	    pushFollow(FOLLOW_viewExpr_in_viewListExpr3066);
            	    viewExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop173;
                }
            } while (true);


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "viewListExpr"


    // $ANTLR start "viewExpr"
    // EsperEPL2Ast.g:519:1: viewExpr : ^(n= VIEW_EXPR IDENT IDENT ( valueExprWithTime )* ) ;
    public final void viewExpr() throws RecognitionException {
        CommonTree n=null;

        try {
            // EsperEPL2Ast.g:520:2: ( ^(n= VIEW_EXPR IDENT IDENT ( valueExprWithTime )* ) )
            // EsperEPL2Ast.g:520:4: ^(n= VIEW_EXPR IDENT IDENT ( valueExprWithTime )* )
            {
            n=(CommonTree)match(input,VIEW_EXPR,FOLLOW_VIEW_EXPR_in_viewExpr3083); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_viewExpr3085); 
            match(input,IDENT,FOLLOW_IDENT_in_viewExpr3087); 
            // EsperEPL2Ast.g:520:30: ( valueExprWithTime )*
            loop174:
            do {
                int alt174=2;
                int LA174_0 = input.LA(1);

                if ( ((LA174_0>=IN_SET && LA174_0<=REGEXP)||LA174_0==NOT_EXPR||(LA174_0>=SUM && LA174_0<=AVG)||(LA174_0>=COALESCE && LA174_0<=COUNT)||(LA174_0>=CASE && LA174_0<=CASE2)||LA174_0==LAST||LA174_0==ISTREAM||(LA174_0>=PREVIOUS && LA174_0<=EXISTS)||(LA174_0>=LW && LA174_0<=CURRENT_TIMESTAMP)||LA174_0==NEWKW||(LA174_0>=NUMERIC_PARAM_RANGE && LA174_0<=OBJECT_PARAM_ORDERED_EXPR)||(LA174_0>=EVAL_AND_EXPR && LA174_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA174_0==EVENT_PROP_EXPR||LA174_0==CONCAT||(LA174_0>=LIB_FUNC_CHAIN && LA174_0<=DOT_EXPR)||(LA174_0>=TIME_PERIOD && LA174_0<=ARRAY_EXPR)||(LA174_0>=NOT_IN_SET && LA174_0<=NOT_REGEXP)||(LA174_0>=IN_RANGE && LA174_0<=SUBSELECT_EXPR)||(LA174_0>=EXISTS_SUBSELECT_EXPR && LA174_0<=NOT_IN_SUBSELECT_EXPR)||(LA174_0>=LAST_OPERATOR && LA174_0<=SUBSTITUTION)||LA174_0==NUMBERSETSTAR||(LA174_0>=FIRST_AGGREG && LA174_0<=WINDOW_AGGREG)||(LA174_0>=INT_TYPE && LA174_0<=NULL_TYPE)||(LA174_0>=JSON_OBJECT && LA174_0<=JSON_ARRAY)||LA174_0==STAR||(LA174_0>=LT && LA174_0<=GT)||(LA174_0>=BOR && LA174_0<=PLUS)||(LA174_0>=BAND && LA174_0<=BXOR)||(LA174_0>=LE && LA174_0<=GE)||(LA174_0>=MINUS && LA174_0<=MOD)||(LA174_0>=EVAL_IS_GROUP_EXPR && LA174_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt174=1;
                }


                switch (alt174) {
            	case 1 :
            	    // EsperEPL2Ast.g:520:31: valueExprWithTime
            	    {
            	    pushFollow(FOLLOW_valueExprWithTime_in_viewExpr3090);
            	    valueExprWithTime();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop174;
                }
            } while (true);

             leaveNode(n); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "viewExpr"


    // $ANTLR start "whereClause"
    // EsperEPL2Ast.g:523:1: whereClause[boolean isLeaveNode] : ^(n= WHERE_EXPR valueExpr ) ;
    public final void whereClause(boolean isLeaveNode) throws RecognitionException {
        CommonTree n=null;

        try {
            // EsperEPL2Ast.g:524:2: ( ^(n= WHERE_EXPR valueExpr ) )
            // EsperEPL2Ast.g:524:4: ^(n= WHERE_EXPR valueExpr )
            {
            n=(CommonTree)match(input,WHERE_EXPR,FOLLOW_WHERE_EXPR_in_whereClause3112); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_whereClause3114);
            valueExpr();

            state._fsp--;

             if (isLeaveNode) leaveNode(n); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "whereClause"


    // $ANTLR start "groupByClause"
    // EsperEPL2Ast.g:527:1: groupByClause : ^(g= GROUP_BY_EXPR valueExpr ( valueExpr )* ) ;
    public final void groupByClause() throws RecognitionException {
        CommonTree g=null;

        try {
            // EsperEPL2Ast.g:528:2: ( ^(g= GROUP_BY_EXPR valueExpr ( valueExpr )* ) )
            // EsperEPL2Ast.g:528:4: ^(g= GROUP_BY_EXPR valueExpr ( valueExpr )* )
            {
            g=(CommonTree)match(input,GROUP_BY_EXPR,FOLLOW_GROUP_BY_EXPR_in_groupByClause3132); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_groupByClause3134);
            valueExpr();

            state._fsp--;

            // EsperEPL2Ast.g:528:32: ( valueExpr )*
            loop175:
            do {
                int alt175=2;
                int LA175_0 = input.LA(1);

                if ( ((LA175_0>=IN_SET && LA175_0<=REGEXP)||LA175_0==NOT_EXPR||(LA175_0>=SUM && LA175_0<=AVG)||(LA175_0>=COALESCE && LA175_0<=COUNT)||(LA175_0>=CASE && LA175_0<=CASE2)||LA175_0==ISTREAM||(LA175_0>=PREVIOUS && LA175_0<=EXISTS)||(LA175_0>=INSTANCEOF && LA175_0<=CURRENT_TIMESTAMP)||LA175_0==NEWKW||(LA175_0>=EVAL_AND_EXPR && LA175_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA175_0==EVENT_PROP_EXPR||LA175_0==CONCAT||(LA175_0>=LIB_FUNC_CHAIN && LA175_0<=DOT_EXPR)||LA175_0==ARRAY_EXPR||(LA175_0>=NOT_IN_SET && LA175_0<=NOT_REGEXP)||(LA175_0>=IN_RANGE && LA175_0<=SUBSELECT_EXPR)||(LA175_0>=EXISTS_SUBSELECT_EXPR && LA175_0<=NOT_IN_SUBSELECT_EXPR)||LA175_0==SUBSTITUTION||(LA175_0>=FIRST_AGGREG && LA175_0<=WINDOW_AGGREG)||(LA175_0>=INT_TYPE && LA175_0<=NULL_TYPE)||(LA175_0>=JSON_OBJECT && LA175_0<=JSON_ARRAY)||LA175_0==STAR||(LA175_0>=LT && LA175_0<=GT)||(LA175_0>=BOR && LA175_0<=PLUS)||(LA175_0>=BAND && LA175_0<=BXOR)||(LA175_0>=LE && LA175_0<=GE)||(LA175_0>=MINUS && LA175_0<=MOD)||(LA175_0>=EVAL_IS_GROUP_EXPR && LA175_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt175=1;
                }


                switch (alt175) {
            	case 1 :
            	    // EsperEPL2Ast.g:528:33: valueExpr
            	    {
            	    pushFollow(FOLLOW_valueExpr_in_groupByClause3137);
            	    valueExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop175;
                }
            } while (true);


            match(input, Token.UP, null); 
             leaveNode(g); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "groupByClause"


    // $ANTLR start "orderByClause"
    // EsperEPL2Ast.g:531:1: orderByClause : ^( ORDER_BY_EXPR orderByElement ( orderByElement )* ) ;
    public final void orderByClause() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:532:2: ( ^( ORDER_BY_EXPR orderByElement ( orderByElement )* ) )
            // EsperEPL2Ast.g:532:4: ^( ORDER_BY_EXPR orderByElement ( orderByElement )* )
            {
            match(input,ORDER_BY_EXPR,FOLLOW_ORDER_BY_EXPR_in_orderByClause3155); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_orderByElement_in_orderByClause3157);
            orderByElement();

            state._fsp--;

            // EsperEPL2Ast.g:532:35: ( orderByElement )*
            loop176:
            do {
                int alt176=2;
                int LA176_0 = input.LA(1);

                if ( (LA176_0==ORDER_ELEMENT_EXPR) ) {
                    alt176=1;
                }


                switch (alt176) {
            	case 1 :
            	    // EsperEPL2Ast.g:532:36: orderByElement
            	    {
            	    pushFollow(FOLLOW_orderByElement_in_orderByClause3160);
            	    orderByElement();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop176;
                }
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "orderByClause"


    // $ANTLR start "orderByElement"
    // EsperEPL2Ast.g:535:1: orderByElement : ^(e= ORDER_ELEMENT_EXPR valueExpr ( ASC | DESC )? ) ;
    public final void orderByElement() throws RecognitionException {
        CommonTree e=null;

        try {
            // EsperEPL2Ast.g:536:2: ( ^(e= ORDER_ELEMENT_EXPR valueExpr ( ASC | DESC )? ) )
            // EsperEPL2Ast.g:536:5: ^(e= ORDER_ELEMENT_EXPR valueExpr ( ASC | DESC )? )
            {
            e=(CommonTree)match(input,ORDER_ELEMENT_EXPR,FOLLOW_ORDER_ELEMENT_EXPR_in_orderByElement3180); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_orderByElement3182);
            valueExpr();

            state._fsp--;

            // EsperEPL2Ast.g:536:38: ( ASC | DESC )?
            int alt177=2;
            int LA177_0 = input.LA(1);

            if ( ((LA177_0>=ASC && LA177_0<=DESC)) ) {
                alt177=1;
            }
            switch (alt177) {
                case 1 :
                    // EsperEPL2Ast.g:
                    {
                    if ( (input.LA(1)>=ASC && input.LA(1)<=DESC) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }

             leaveNode(e); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "orderByElement"


    // $ANTLR start "havingClause"
    // EsperEPL2Ast.g:539:1: havingClause : ^(n= HAVING_EXPR valueExpr ) ;
    public final void havingClause() throws RecognitionException {
        CommonTree n=null;

        try {
            // EsperEPL2Ast.g:540:2: ( ^(n= HAVING_EXPR valueExpr ) )
            // EsperEPL2Ast.g:540:4: ^(n= HAVING_EXPR valueExpr )
            {
            n=(CommonTree)match(input,HAVING_EXPR,FOLLOW_HAVING_EXPR_in_havingClause3207); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_havingClause3209);
            valueExpr();

            state._fsp--;

             leaveNode(n); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "havingClause"


    // $ANTLR start "outputLimitExpr"
    // EsperEPL2Ast.g:543:1: outputLimitExpr : ( ^(e= EVENT_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? ( number | IDENT ) ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(tp= TIMEPERIOD_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? timePeriod ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(cron= CRONTAB_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? crontabLimitParameterSet ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(when= WHEN_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? valueExpr ( onSetExpr )? ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(term= TERM_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? outputLimitAndTerm ( onSetExpr )? ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(after= AFTER_LIMIT_EXPR outputLimitAfter ( outputLimitAndTerm )? ) );
    public final void outputLimitExpr() throws RecognitionException {
        CommonTree e=null;
        CommonTree tp=null;
        CommonTree cron=null;
        CommonTree when=null;
        CommonTree term=null;
        CommonTree after=null;

        try {
            // EsperEPL2Ast.g:544:2: ( ^(e= EVENT_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? ( number | IDENT ) ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(tp= TIMEPERIOD_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? timePeriod ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(cron= CRONTAB_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? crontabLimitParameterSet ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(when= WHEN_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? valueExpr ( onSetExpr )? ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(term= TERM_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? outputLimitAndTerm ( onSetExpr )? ( outputLimitAfter )? ( outputLimitAndTerm )? ) | ^(after= AFTER_LIMIT_EXPR outputLimitAfter ( outputLimitAndTerm )? ) )
            int alt197=6;
            switch ( input.LA(1) ) {
            case EVENT_LIMIT_EXPR:
                {
                alt197=1;
                }
                break;
            case TIMEPERIOD_LIMIT_EXPR:
                {
                alt197=2;
                }
                break;
            case CRONTAB_LIMIT_EXPR:
                {
                alt197=3;
                }
                break;
            case WHEN_LIMIT_EXPR:
                {
                alt197=4;
                }
                break;
            case TERM_LIMIT_EXPR:
                {
                alt197=5;
                }
                break;
            case AFTER_LIMIT_EXPR:
                {
                alt197=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 197, 0, input);

                throw nvae;
            }

            switch (alt197) {
                case 1 :
                    // EsperEPL2Ast.g:544:4: ^(e= EVENT_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? ( number | IDENT ) ( outputLimitAfter )? ( outputLimitAndTerm )? )
                    {
                    e=(CommonTree)match(input,EVENT_LIMIT_EXPR,FOLLOW_EVENT_LIMIT_EXPR_in_outputLimitExpr3227); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:544:25: ( ALL | FIRST | LAST | SNAPSHOT )?
                    int alt178=2;
                    int LA178_0 = input.LA(1);

                    if ( (LA178_0==ALL||(LA178_0>=FIRST && LA178_0<=LAST)||LA178_0==SNAPSHOT) ) {
                        alt178=1;
                    }
                    switch (alt178) {
                        case 1 :
                            // EsperEPL2Ast.g:
                            {
                            if ( input.LA(1)==ALL||(input.LA(1)>=FIRST && input.LA(1)<=LAST)||input.LA(1)==SNAPSHOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:544:52: ( number | IDENT )
                    int alt179=2;
                    int LA179_0 = input.LA(1);

                    if ( ((LA179_0>=INT_TYPE && LA179_0<=DOUBLE_TYPE)) ) {
                        alt179=1;
                    }
                    else if ( (LA179_0==IDENT) ) {
                        alt179=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 179, 0, input);

                        throw nvae;
                    }
                    switch (alt179) {
                        case 1 :
                            // EsperEPL2Ast.g:544:53: number
                            {
                            pushFollow(FOLLOW_number_in_outputLimitExpr3241);
                            number();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:544:60: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_outputLimitExpr3243); 

                            }
                            break;

                    }

                    // EsperEPL2Ast.g:544:67: ( outputLimitAfter )?
                    int alt180=2;
                    int LA180_0 = input.LA(1);

                    if ( (LA180_0==AFTER) ) {
                        alt180=1;
                    }
                    switch (alt180) {
                        case 1 :
                            // EsperEPL2Ast.g:544:67: outputLimitAfter
                            {
                            pushFollow(FOLLOW_outputLimitAfter_in_outputLimitExpr3246);
                            outputLimitAfter();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:544:85: ( outputLimitAndTerm )?
                    int alt181=2;
                    int LA181_0 = input.LA(1);

                    if ( (LA181_0==TERMINATED) ) {
                        alt181=1;
                    }
                    switch (alt181) {
                        case 1 :
                            // EsperEPL2Ast.g:544:85: outputLimitAndTerm
                            {
                            pushFollow(FOLLOW_outputLimitAndTerm_in_outputLimitExpr3249);
                            outputLimitAndTerm();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(e); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:545:7: ^(tp= TIMEPERIOD_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? timePeriod ( outputLimitAfter )? ( outputLimitAndTerm )? )
                    {
                    tp=(CommonTree)match(input,TIMEPERIOD_LIMIT_EXPR,FOLLOW_TIMEPERIOD_LIMIT_EXPR_in_outputLimitExpr3266); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:545:34: ( ALL | FIRST | LAST | SNAPSHOT )?
                    int alt182=2;
                    int LA182_0 = input.LA(1);

                    if ( (LA182_0==ALL||(LA182_0>=FIRST && LA182_0<=LAST)||LA182_0==SNAPSHOT) ) {
                        alt182=1;
                    }
                    switch (alt182) {
                        case 1 :
                            // EsperEPL2Ast.g:
                            {
                            if ( input.LA(1)==ALL||(input.LA(1)>=FIRST && input.LA(1)<=LAST)||input.LA(1)==SNAPSHOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }

                    pushFollow(FOLLOW_timePeriod_in_outputLimitExpr3279);
                    timePeriod();

                    state._fsp--;

                    // EsperEPL2Ast.g:545:72: ( outputLimitAfter )?
                    int alt183=2;
                    int LA183_0 = input.LA(1);

                    if ( (LA183_0==AFTER) ) {
                        alt183=1;
                    }
                    switch (alt183) {
                        case 1 :
                            // EsperEPL2Ast.g:545:72: outputLimitAfter
                            {
                            pushFollow(FOLLOW_outputLimitAfter_in_outputLimitExpr3281);
                            outputLimitAfter();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:545:90: ( outputLimitAndTerm )?
                    int alt184=2;
                    int LA184_0 = input.LA(1);

                    if ( (LA184_0==TERMINATED) ) {
                        alt184=1;
                    }
                    switch (alt184) {
                        case 1 :
                            // EsperEPL2Ast.g:545:90: outputLimitAndTerm
                            {
                            pushFollow(FOLLOW_outputLimitAndTerm_in_outputLimitExpr3284);
                            outputLimitAndTerm();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(tp); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:546:7: ^(cron= CRONTAB_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? crontabLimitParameterSet ( outputLimitAfter )? ( outputLimitAndTerm )? )
                    {
                    cron=(CommonTree)match(input,CRONTAB_LIMIT_EXPR,FOLLOW_CRONTAB_LIMIT_EXPR_in_outputLimitExpr3300); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:546:33: ( ALL | FIRST | LAST | SNAPSHOT )?
                    int alt185=2;
                    int LA185_0 = input.LA(1);

                    if ( (LA185_0==ALL||(LA185_0>=FIRST && LA185_0<=LAST)||LA185_0==SNAPSHOT) ) {
                        alt185=1;
                    }
                    switch (alt185) {
                        case 1 :
                            // EsperEPL2Ast.g:
                            {
                            if ( input.LA(1)==ALL||(input.LA(1)>=FIRST && input.LA(1)<=LAST)||input.LA(1)==SNAPSHOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }

                    pushFollow(FOLLOW_crontabLimitParameterSet_in_outputLimitExpr3313);
                    crontabLimitParameterSet();

                    state._fsp--;

                    // EsperEPL2Ast.g:546:85: ( outputLimitAfter )?
                    int alt186=2;
                    int LA186_0 = input.LA(1);

                    if ( (LA186_0==AFTER) ) {
                        alt186=1;
                    }
                    switch (alt186) {
                        case 1 :
                            // EsperEPL2Ast.g:546:85: outputLimitAfter
                            {
                            pushFollow(FOLLOW_outputLimitAfter_in_outputLimitExpr3315);
                            outputLimitAfter();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:546:103: ( outputLimitAndTerm )?
                    int alt187=2;
                    int LA187_0 = input.LA(1);

                    if ( (LA187_0==TERMINATED) ) {
                        alt187=1;
                    }
                    switch (alt187) {
                        case 1 :
                            // EsperEPL2Ast.g:546:103: outputLimitAndTerm
                            {
                            pushFollow(FOLLOW_outputLimitAndTerm_in_outputLimitExpr3318);
                            outputLimitAndTerm();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(cron); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:547:7: ^(when= WHEN_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? valueExpr ( onSetExpr )? ( outputLimitAfter )? ( outputLimitAndTerm )? )
                    {
                    when=(CommonTree)match(input,WHEN_LIMIT_EXPR,FOLLOW_WHEN_LIMIT_EXPR_in_outputLimitExpr3334); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:547:30: ( ALL | FIRST | LAST | SNAPSHOT )?
                    int alt188=2;
                    int LA188_0 = input.LA(1);

                    if ( (LA188_0==ALL||(LA188_0>=FIRST && LA188_0<=LAST)||LA188_0==SNAPSHOT) ) {
                        alt188=1;
                    }
                    switch (alt188) {
                        case 1 :
                            // EsperEPL2Ast.g:
                            {
                            if ( input.LA(1)==ALL||(input.LA(1)>=FIRST && input.LA(1)<=LAST)||input.LA(1)==SNAPSHOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }

                    pushFollow(FOLLOW_valueExpr_in_outputLimitExpr3347);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:547:67: ( onSetExpr )?
                    int alt189=2;
                    int LA189_0 = input.LA(1);

                    if ( (LA189_0==ON_SET_EXPR) ) {
                        alt189=1;
                    }
                    switch (alt189) {
                        case 1 :
                            // EsperEPL2Ast.g:547:67: onSetExpr
                            {
                            pushFollow(FOLLOW_onSetExpr_in_outputLimitExpr3349);
                            onSetExpr();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:547:78: ( outputLimitAfter )?
                    int alt190=2;
                    int LA190_0 = input.LA(1);

                    if ( (LA190_0==AFTER) ) {
                        alt190=1;
                    }
                    switch (alt190) {
                        case 1 :
                            // EsperEPL2Ast.g:547:78: outputLimitAfter
                            {
                            pushFollow(FOLLOW_outputLimitAfter_in_outputLimitExpr3352);
                            outputLimitAfter();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:547:96: ( outputLimitAndTerm )?
                    int alt191=2;
                    int LA191_0 = input.LA(1);

                    if ( (LA191_0==TERMINATED) ) {
                        alt191=1;
                    }
                    switch (alt191) {
                        case 1 :
                            // EsperEPL2Ast.g:547:96: outputLimitAndTerm
                            {
                            pushFollow(FOLLOW_outputLimitAndTerm_in_outputLimitExpr3355);
                            outputLimitAndTerm();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(when); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:548:7: ^(term= TERM_LIMIT_EXPR ( ALL | FIRST | LAST | SNAPSHOT )? outputLimitAndTerm ( onSetExpr )? ( outputLimitAfter )? ( outputLimitAndTerm )? )
                    {
                    term=(CommonTree)match(input,TERM_LIMIT_EXPR,FOLLOW_TERM_LIMIT_EXPR_in_outputLimitExpr3371); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:548:30: ( ALL | FIRST | LAST | SNAPSHOT )?
                    int alt192=2;
                    int LA192_0 = input.LA(1);

                    if ( (LA192_0==ALL||(LA192_0>=FIRST && LA192_0<=LAST)||LA192_0==SNAPSHOT) ) {
                        alt192=1;
                    }
                    switch (alt192) {
                        case 1 :
                            // EsperEPL2Ast.g:
                            {
                            if ( input.LA(1)==ALL||(input.LA(1)>=FIRST && input.LA(1)<=LAST)||input.LA(1)==SNAPSHOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }

                    pushFollow(FOLLOW_outputLimitAndTerm_in_outputLimitExpr3384);
                    outputLimitAndTerm();

                    state._fsp--;

                    // EsperEPL2Ast.g:548:76: ( onSetExpr )?
                    int alt193=2;
                    int LA193_0 = input.LA(1);

                    if ( (LA193_0==ON_SET_EXPR) ) {
                        alt193=1;
                    }
                    switch (alt193) {
                        case 1 :
                            // EsperEPL2Ast.g:548:76: onSetExpr
                            {
                            pushFollow(FOLLOW_onSetExpr_in_outputLimitExpr3386);
                            onSetExpr();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:548:87: ( outputLimitAfter )?
                    int alt194=2;
                    int LA194_0 = input.LA(1);

                    if ( (LA194_0==AFTER) ) {
                        alt194=1;
                    }
                    switch (alt194) {
                        case 1 :
                            // EsperEPL2Ast.g:548:87: outputLimitAfter
                            {
                            pushFollow(FOLLOW_outputLimitAfter_in_outputLimitExpr3389);
                            outputLimitAfter();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:548:105: ( outputLimitAndTerm )?
                    int alt195=2;
                    int LA195_0 = input.LA(1);

                    if ( (LA195_0==TERMINATED) ) {
                        alt195=1;
                    }
                    switch (alt195) {
                        case 1 :
                            // EsperEPL2Ast.g:548:105: outputLimitAndTerm
                            {
                            pushFollow(FOLLOW_outputLimitAndTerm_in_outputLimitExpr3392);
                            outputLimitAndTerm();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(term); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:549:4: ^(after= AFTER_LIMIT_EXPR outputLimitAfter ( outputLimitAndTerm )? )
                    {
                    after=(CommonTree)match(input,AFTER_LIMIT_EXPR,FOLLOW_AFTER_LIMIT_EXPR_in_outputLimitExpr3405); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_outputLimitAfter_in_outputLimitExpr3407);
                    outputLimitAfter();

                    state._fsp--;

                    // EsperEPL2Ast.g:549:46: ( outputLimitAndTerm )?
                    int alt196=2;
                    int LA196_0 = input.LA(1);

                    if ( (LA196_0==TERMINATED) ) {
                        alt196=1;
                    }
                    switch (alt196) {
                        case 1 :
                            // EsperEPL2Ast.g:549:46: outputLimitAndTerm
                            {
                            pushFollow(FOLLOW_outputLimitAndTerm_in_outputLimitExpr3409);
                            outputLimitAndTerm();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(after); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "outputLimitExpr"


    // $ANTLR start "outputLimitAndTerm"
    // EsperEPL2Ast.g:552:1: outputLimitAndTerm : ^( TERMINATED ( valueExpr )? ( onSetExpr )? ) ;
    public final void outputLimitAndTerm() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:553:2: ( ^( TERMINATED ( valueExpr )? ( onSetExpr )? ) )
            // EsperEPL2Ast.g:553:5: ^( TERMINATED ( valueExpr )? ( onSetExpr )? )
            {
            match(input,TERMINATED,FOLLOW_TERMINATED_in_outputLimitAndTerm3426); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:553:18: ( valueExpr )?
                int alt198=2;
                int LA198_0 = input.LA(1);

                if ( ((LA198_0>=IN_SET && LA198_0<=REGEXP)||LA198_0==NOT_EXPR||(LA198_0>=SUM && LA198_0<=AVG)||(LA198_0>=COALESCE && LA198_0<=COUNT)||(LA198_0>=CASE && LA198_0<=CASE2)||LA198_0==ISTREAM||(LA198_0>=PREVIOUS && LA198_0<=EXISTS)||(LA198_0>=INSTANCEOF && LA198_0<=CURRENT_TIMESTAMP)||LA198_0==NEWKW||(LA198_0>=EVAL_AND_EXPR && LA198_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA198_0==EVENT_PROP_EXPR||LA198_0==CONCAT||(LA198_0>=LIB_FUNC_CHAIN && LA198_0<=DOT_EXPR)||LA198_0==ARRAY_EXPR||(LA198_0>=NOT_IN_SET && LA198_0<=NOT_REGEXP)||(LA198_0>=IN_RANGE && LA198_0<=SUBSELECT_EXPR)||(LA198_0>=EXISTS_SUBSELECT_EXPR && LA198_0<=NOT_IN_SUBSELECT_EXPR)||LA198_0==SUBSTITUTION||(LA198_0>=FIRST_AGGREG && LA198_0<=WINDOW_AGGREG)||(LA198_0>=INT_TYPE && LA198_0<=NULL_TYPE)||(LA198_0>=JSON_OBJECT && LA198_0<=JSON_ARRAY)||LA198_0==STAR||(LA198_0>=LT && LA198_0<=GT)||(LA198_0>=BOR && LA198_0<=PLUS)||(LA198_0>=BAND && LA198_0<=BXOR)||(LA198_0>=LE && LA198_0<=GE)||(LA198_0>=MINUS && LA198_0<=MOD)||(LA198_0>=EVAL_IS_GROUP_EXPR && LA198_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt198=1;
                }
                switch (alt198) {
                    case 1 :
                        // EsperEPL2Ast.g:553:18: valueExpr
                        {
                        pushFollow(FOLLOW_valueExpr_in_outputLimitAndTerm3428);
                        valueExpr();

                        state._fsp--;


                        }
                        break;

                }

                // EsperEPL2Ast.g:553:29: ( onSetExpr )?
                int alt199=2;
                int LA199_0 = input.LA(1);

                if ( (LA199_0==ON_SET_EXPR) ) {
                    alt199=1;
                }
                switch (alt199) {
                    case 1 :
                        // EsperEPL2Ast.g:553:29: onSetExpr
                        {
                        pushFollow(FOLLOW_onSetExpr_in_outputLimitAndTerm3431);
                        onSetExpr();

                        state._fsp--;


                        }
                        break;

                }


                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "outputLimitAndTerm"


    // $ANTLR start "outputLimitAfter"
    // EsperEPL2Ast.g:556:1: outputLimitAfter : ^( AFTER ( timePeriod )? ( number )? ) ;
    public final void outputLimitAfter() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:557:2: ( ^( AFTER ( timePeriod )? ( number )? ) )
            // EsperEPL2Ast.g:557:4: ^( AFTER ( timePeriod )? ( number )? )
            {
            match(input,AFTER,FOLLOW_AFTER_in_outputLimitAfter3445); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:557:12: ( timePeriod )?
                int alt200=2;
                int LA200_0 = input.LA(1);

                if ( (LA200_0==TIME_PERIOD) ) {
                    alt200=1;
                }
                switch (alt200) {
                    case 1 :
                        // EsperEPL2Ast.g:557:12: timePeriod
                        {
                        pushFollow(FOLLOW_timePeriod_in_outputLimitAfter3447);
                        timePeriod();

                        state._fsp--;


                        }
                        break;

                }

                // EsperEPL2Ast.g:557:24: ( number )?
                int alt201=2;
                int LA201_0 = input.LA(1);

                if ( ((LA201_0>=INT_TYPE && LA201_0<=DOUBLE_TYPE)) ) {
                    alt201=1;
                }
                switch (alt201) {
                    case 1 :
                        // EsperEPL2Ast.g:557:24: number
                        {
                        pushFollow(FOLLOW_number_in_outputLimitAfter3450);
                        number();

                        state._fsp--;


                        }
                        break;

                }


                match(input, Token.UP, null); 
            }

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "outputLimitAfter"


    // $ANTLR start "rowLimitClause"
    // EsperEPL2Ast.g:560:1: rowLimitClause : ^(e= ROW_LIMIT_EXPR ( number | IDENT ) ( number | IDENT )? ( COMMA )? ( OFFSET )? ) ;
    public final void rowLimitClause() throws RecognitionException {
        CommonTree e=null;

        try {
            // EsperEPL2Ast.g:561:2: ( ^(e= ROW_LIMIT_EXPR ( number | IDENT ) ( number | IDENT )? ( COMMA )? ( OFFSET )? ) )
            // EsperEPL2Ast.g:561:4: ^(e= ROW_LIMIT_EXPR ( number | IDENT ) ( number | IDENT )? ( COMMA )? ( OFFSET )? )
            {
            e=(CommonTree)match(input,ROW_LIMIT_EXPR,FOLLOW_ROW_LIMIT_EXPR_in_rowLimitClause3466); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:561:23: ( number | IDENT )
            int alt202=2;
            int LA202_0 = input.LA(1);

            if ( ((LA202_0>=INT_TYPE && LA202_0<=DOUBLE_TYPE)) ) {
                alt202=1;
            }
            else if ( (LA202_0==IDENT) ) {
                alt202=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 202, 0, input);

                throw nvae;
            }
            switch (alt202) {
                case 1 :
                    // EsperEPL2Ast.g:561:24: number
                    {
                    pushFollow(FOLLOW_number_in_rowLimitClause3469);
                    number();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:561:31: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_rowLimitClause3471); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:561:38: ( number | IDENT )?
            int alt203=3;
            int LA203_0 = input.LA(1);

            if ( ((LA203_0>=INT_TYPE && LA203_0<=DOUBLE_TYPE)) ) {
                alt203=1;
            }
            else if ( (LA203_0==IDENT) ) {
                alt203=2;
            }
            switch (alt203) {
                case 1 :
                    // EsperEPL2Ast.g:561:39: number
                    {
                    pushFollow(FOLLOW_number_in_rowLimitClause3475);
                    number();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:561:46: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_rowLimitClause3477); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:561:54: ( COMMA )?
            int alt204=2;
            int LA204_0 = input.LA(1);

            if ( (LA204_0==COMMA) ) {
                alt204=1;
            }
            switch (alt204) {
                case 1 :
                    // EsperEPL2Ast.g:561:54: COMMA
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_rowLimitClause3481); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:561:61: ( OFFSET )?
            int alt205=2;
            int LA205_0 = input.LA(1);

            if ( (LA205_0==OFFSET) ) {
                alt205=1;
            }
            switch (alt205) {
                case 1 :
                    // EsperEPL2Ast.g:561:61: OFFSET
                    {
                    match(input,OFFSET,FOLLOW_OFFSET_in_rowLimitClause3484); 

                    }
                    break;

            }

             leaveNode(e); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "rowLimitClause"


    // $ANTLR start "crontabLimitParameterSet"
    // EsperEPL2Ast.g:564:1: crontabLimitParameterSet : ^( CRONTAB_LIMIT_EXPR_PARAM valueExprWithTime valueExprWithTime valueExprWithTime valueExprWithTime valueExprWithTime ( valueExprWithTime )? ) ;
    public final void crontabLimitParameterSet() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:565:2: ( ^( CRONTAB_LIMIT_EXPR_PARAM valueExprWithTime valueExprWithTime valueExprWithTime valueExprWithTime valueExprWithTime ( valueExprWithTime )? ) )
            // EsperEPL2Ast.g:565:4: ^( CRONTAB_LIMIT_EXPR_PARAM valueExprWithTime valueExprWithTime valueExprWithTime valueExprWithTime valueExprWithTime ( valueExprWithTime )? )
            {
            match(input,CRONTAB_LIMIT_EXPR_PARAM,FOLLOW_CRONTAB_LIMIT_EXPR_PARAM_in_crontabLimitParameterSet3502); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3504);
            valueExprWithTime();

            state._fsp--;

            pushFollow(FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3506);
            valueExprWithTime();

            state._fsp--;

            pushFollow(FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3508);
            valueExprWithTime();

            state._fsp--;

            pushFollow(FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3510);
            valueExprWithTime();

            state._fsp--;

            pushFollow(FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3512);
            valueExprWithTime();

            state._fsp--;

            // EsperEPL2Ast.g:565:121: ( valueExprWithTime )?
            int alt206=2;
            int LA206_0 = input.LA(1);

            if ( ((LA206_0>=IN_SET && LA206_0<=REGEXP)||LA206_0==NOT_EXPR||(LA206_0>=SUM && LA206_0<=AVG)||(LA206_0>=COALESCE && LA206_0<=COUNT)||(LA206_0>=CASE && LA206_0<=CASE2)||LA206_0==LAST||LA206_0==ISTREAM||(LA206_0>=PREVIOUS && LA206_0<=EXISTS)||(LA206_0>=LW && LA206_0<=CURRENT_TIMESTAMP)||LA206_0==NEWKW||(LA206_0>=NUMERIC_PARAM_RANGE && LA206_0<=OBJECT_PARAM_ORDERED_EXPR)||(LA206_0>=EVAL_AND_EXPR && LA206_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA206_0==EVENT_PROP_EXPR||LA206_0==CONCAT||(LA206_0>=LIB_FUNC_CHAIN && LA206_0<=DOT_EXPR)||(LA206_0>=TIME_PERIOD && LA206_0<=ARRAY_EXPR)||(LA206_0>=NOT_IN_SET && LA206_0<=NOT_REGEXP)||(LA206_0>=IN_RANGE && LA206_0<=SUBSELECT_EXPR)||(LA206_0>=EXISTS_SUBSELECT_EXPR && LA206_0<=NOT_IN_SUBSELECT_EXPR)||(LA206_0>=LAST_OPERATOR && LA206_0<=SUBSTITUTION)||LA206_0==NUMBERSETSTAR||(LA206_0>=FIRST_AGGREG && LA206_0<=WINDOW_AGGREG)||(LA206_0>=INT_TYPE && LA206_0<=NULL_TYPE)||(LA206_0>=JSON_OBJECT && LA206_0<=JSON_ARRAY)||LA206_0==STAR||(LA206_0>=LT && LA206_0<=GT)||(LA206_0>=BOR && LA206_0<=PLUS)||(LA206_0>=BAND && LA206_0<=BXOR)||(LA206_0>=LE && LA206_0<=GE)||(LA206_0>=MINUS && LA206_0<=MOD)||(LA206_0>=EVAL_IS_GROUP_EXPR && LA206_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt206=1;
            }
            switch (alt206) {
                case 1 :
                    // EsperEPL2Ast.g:565:121: valueExprWithTime
                    {
                    pushFollow(FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3514);
                    valueExprWithTime();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "crontabLimitParameterSet"


    // $ANTLR start "relationalExpr"
    // EsperEPL2Ast.g:568:1: relationalExpr : ( ^(n= LT relationalExprValue ) | ^(n= GT relationalExprValue ) | ^(n= LE relationalExprValue ) | ^(n= GE relationalExprValue ) );
    public final void relationalExpr() throws RecognitionException {
        CommonTree n=null;

        try {
            // EsperEPL2Ast.g:569:2: ( ^(n= LT relationalExprValue ) | ^(n= GT relationalExprValue ) | ^(n= LE relationalExprValue ) | ^(n= GE relationalExprValue ) )
            int alt207=4;
            switch ( input.LA(1) ) {
            case LT:
                {
                alt207=1;
                }
                break;
            case GT:
                {
                alt207=2;
                }
                break;
            case LE:
                {
                alt207=3;
                }
                break;
            case GE:
                {
                alt207=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 207, 0, input);

                throw nvae;
            }

            switch (alt207) {
                case 1 :
                    // EsperEPL2Ast.g:569:5: ^(n= LT relationalExprValue )
                    {
                    n=(CommonTree)match(input,LT,FOLLOW_LT_in_relationalExpr3531); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_relationalExprValue_in_relationalExpr3533);
                    relationalExprValue();

                    state._fsp--;

                     leaveNode(n); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:570:5: ^(n= GT relationalExprValue )
                    {
                    n=(CommonTree)match(input,GT,FOLLOW_GT_in_relationalExpr3546); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_relationalExprValue_in_relationalExpr3548);
                    relationalExprValue();

                    state._fsp--;

                     leaveNode(n); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:571:5: ^(n= LE relationalExprValue )
                    {
                    n=(CommonTree)match(input,LE,FOLLOW_LE_in_relationalExpr3561); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_relationalExprValue_in_relationalExpr3563);
                    relationalExprValue();

                    state._fsp--;

                     leaveNode(n); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:572:4: ^(n= GE relationalExprValue )
                    {
                    n=(CommonTree)match(input,GE,FOLLOW_GE_in_relationalExpr3575); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_relationalExprValue_in_relationalExpr3577);
                    relationalExprValue();

                    state._fsp--;

                     leaveNode(n); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "relationalExpr"


    // $ANTLR start "relationalExprValue"
    // EsperEPL2Ast.g:575:1: relationalExprValue : ( valueExpr ( valueExpr | ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) ) ) ;
    public final void relationalExprValue() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:576:2: ( ( valueExpr ( valueExpr | ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) ) ) )
            // EsperEPL2Ast.g:576:4: ( valueExpr ( valueExpr | ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) ) )
            {
            // EsperEPL2Ast.g:576:4: ( valueExpr ( valueExpr | ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) ) )
            // EsperEPL2Ast.g:577:5: valueExpr ( valueExpr | ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) )
            {
            pushFollow(FOLLOW_valueExpr_in_relationalExprValue3599);
            valueExpr();

            state._fsp--;

            // EsperEPL2Ast.g:578:6: ( valueExpr | ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) )
            int alt210=2;
            int LA210_0 = input.LA(1);

            if ( ((LA210_0>=IN_SET && LA210_0<=REGEXP)||LA210_0==NOT_EXPR||(LA210_0>=SUM && LA210_0<=AVG)||(LA210_0>=COALESCE && LA210_0<=COUNT)||(LA210_0>=CASE && LA210_0<=CASE2)||LA210_0==ISTREAM||(LA210_0>=PREVIOUS && LA210_0<=EXISTS)||(LA210_0>=INSTANCEOF && LA210_0<=CURRENT_TIMESTAMP)||LA210_0==NEWKW||(LA210_0>=EVAL_AND_EXPR && LA210_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA210_0==EVENT_PROP_EXPR||LA210_0==CONCAT||(LA210_0>=LIB_FUNC_CHAIN && LA210_0<=DOT_EXPR)||LA210_0==ARRAY_EXPR||(LA210_0>=NOT_IN_SET && LA210_0<=NOT_REGEXP)||(LA210_0>=IN_RANGE && LA210_0<=SUBSELECT_EXPR)||(LA210_0>=EXISTS_SUBSELECT_EXPR && LA210_0<=NOT_IN_SUBSELECT_EXPR)||LA210_0==SUBSTITUTION||(LA210_0>=FIRST_AGGREG && LA210_0<=WINDOW_AGGREG)||(LA210_0>=INT_TYPE && LA210_0<=NULL_TYPE)||(LA210_0>=JSON_OBJECT && LA210_0<=JSON_ARRAY)||LA210_0==STAR||(LA210_0>=LT && LA210_0<=GT)||(LA210_0>=BOR && LA210_0<=PLUS)||(LA210_0>=BAND && LA210_0<=BXOR)||(LA210_0>=LE && LA210_0<=GE)||(LA210_0>=MINUS && LA210_0<=MOD)||(LA210_0>=EVAL_IS_GROUP_EXPR && LA210_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt210=1;
            }
            else if ( ((LA210_0>=ALL && LA210_0<=SOME)) ) {
                alt210=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 210, 0, input);

                throw nvae;
            }
            switch (alt210) {
                case 1 :
                    // EsperEPL2Ast.g:578:8: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_relationalExprValue3609);
                    valueExpr();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:580:6: ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr )
                    {
                    if ( (input.LA(1)>=ALL && input.LA(1)<=SOME) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // EsperEPL2Ast.g:580:21: ( ( valueExpr )* | subSelectGroupExpr )
                    int alt209=2;
                    int LA209_0 = input.LA(1);

                    if ( (LA209_0==UP||(LA209_0>=IN_SET && LA209_0<=REGEXP)||LA209_0==NOT_EXPR||(LA209_0>=SUM && LA209_0<=AVG)||(LA209_0>=COALESCE && LA209_0<=COUNT)||(LA209_0>=CASE && LA209_0<=CASE2)||LA209_0==ISTREAM||(LA209_0>=PREVIOUS && LA209_0<=EXISTS)||(LA209_0>=INSTANCEOF && LA209_0<=CURRENT_TIMESTAMP)||LA209_0==NEWKW||(LA209_0>=EVAL_AND_EXPR && LA209_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA209_0==EVENT_PROP_EXPR||LA209_0==CONCAT||(LA209_0>=LIB_FUNC_CHAIN && LA209_0<=DOT_EXPR)||LA209_0==ARRAY_EXPR||(LA209_0>=NOT_IN_SET && LA209_0<=NOT_REGEXP)||(LA209_0>=IN_RANGE && LA209_0<=SUBSELECT_EXPR)||(LA209_0>=EXISTS_SUBSELECT_EXPR && LA209_0<=NOT_IN_SUBSELECT_EXPR)||LA209_0==SUBSTITUTION||(LA209_0>=FIRST_AGGREG && LA209_0<=WINDOW_AGGREG)||(LA209_0>=INT_TYPE && LA209_0<=NULL_TYPE)||(LA209_0>=JSON_OBJECT && LA209_0<=JSON_ARRAY)||LA209_0==STAR||(LA209_0>=LT && LA209_0<=GT)||(LA209_0>=BOR && LA209_0<=PLUS)||(LA209_0>=BAND && LA209_0<=BXOR)||(LA209_0>=LE && LA209_0<=GE)||(LA209_0>=MINUS && LA209_0<=MOD)||(LA209_0>=EVAL_IS_GROUP_EXPR && LA209_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                        alt209=1;
                    }
                    else if ( (LA209_0==SUBSELECT_GROUP_EXPR) ) {
                        alt209=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 209, 0, input);

                        throw nvae;
                    }
                    switch (alt209) {
                        case 1 :
                            // EsperEPL2Ast.g:580:22: ( valueExpr )*
                            {
                            // EsperEPL2Ast.g:580:22: ( valueExpr )*
                            loop208:
                            do {
                                int alt208=2;
                                int LA208_0 = input.LA(1);

                                if ( ((LA208_0>=IN_SET && LA208_0<=REGEXP)||LA208_0==NOT_EXPR||(LA208_0>=SUM && LA208_0<=AVG)||(LA208_0>=COALESCE && LA208_0<=COUNT)||(LA208_0>=CASE && LA208_0<=CASE2)||LA208_0==ISTREAM||(LA208_0>=PREVIOUS && LA208_0<=EXISTS)||(LA208_0>=INSTANCEOF && LA208_0<=CURRENT_TIMESTAMP)||LA208_0==NEWKW||(LA208_0>=EVAL_AND_EXPR && LA208_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA208_0==EVENT_PROP_EXPR||LA208_0==CONCAT||(LA208_0>=LIB_FUNC_CHAIN && LA208_0<=DOT_EXPR)||LA208_0==ARRAY_EXPR||(LA208_0>=NOT_IN_SET && LA208_0<=NOT_REGEXP)||(LA208_0>=IN_RANGE && LA208_0<=SUBSELECT_EXPR)||(LA208_0>=EXISTS_SUBSELECT_EXPR && LA208_0<=NOT_IN_SUBSELECT_EXPR)||LA208_0==SUBSTITUTION||(LA208_0>=FIRST_AGGREG && LA208_0<=WINDOW_AGGREG)||(LA208_0>=INT_TYPE && LA208_0<=NULL_TYPE)||(LA208_0>=JSON_OBJECT && LA208_0<=JSON_ARRAY)||LA208_0==STAR||(LA208_0>=LT && LA208_0<=GT)||(LA208_0>=BOR && LA208_0<=PLUS)||(LA208_0>=BAND && LA208_0<=BXOR)||(LA208_0>=LE && LA208_0<=GE)||(LA208_0>=MINUS && LA208_0<=MOD)||(LA208_0>=EVAL_IS_GROUP_EXPR && LA208_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                                    alt208=1;
                                }


                                switch (alt208) {
                            	case 1 :
                            	    // EsperEPL2Ast.g:580:22: valueExpr
                            	    {
                            	    pushFollow(FOLLOW_valueExpr_in_relationalExprValue3633);
                            	    valueExpr();

                            	    state._fsp--;


                            	    }
                            	    break;

                            	default :
                            	    break loop208;
                                }
                            } while (true);


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:580:35: subSelectGroupExpr
                            {
                            pushFollow(FOLLOW_subSelectGroupExpr_in_relationalExprValue3638);
                            subSelectGroupExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;

            }


            }


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "relationalExprValue"


    // $ANTLR start "evalExprChoice"
    // EsperEPL2Ast.g:585:1: evalExprChoice : ( ^(jo= EVAL_OR_EXPR valueExpr valueExpr ( valueExpr )* ) | ^(ja= EVAL_AND_EXPR valueExpr valueExpr ( valueExpr )* ) | ^(je= EVAL_EQUALS_EXPR valueExpr valueExpr ) | ^(ji= EVAL_IS_EXPR valueExpr valueExpr ) | ^(jne= EVAL_NOTEQUALS_EXPR valueExpr valueExpr ) | ^(jis= EVAL_ISNOT_EXPR valueExpr valueExpr ) | ^(jge= EVAL_EQUALS_GROUP_EXPR equalsSubquery ) | ^(jgi= EVAL_IS_GROUP_EXPR equalsSubquery ) | ^(jgne= EVAL_NOTEQUALS_GROUP_EXPR equalsSubquery ) | ^(jgni= EVAL_ISNOT_GROUP_EXPR equalsSubquery ) | ^(n= NOT_EXPR valueExpr ) | r= relationalExpr );
    public final void evalExprChoice() throws RecognitionException {
        CommonTree jo=null;
        CommonTree ja=null;
        CommonTree je=null;
        CommonTree ji=null;
        CommonTree jne=null;
        CommonTree jis=null;
        CommonTree jge=null;
        CommonTree jgi=null;
        CommonTree jgne=null;
        CommonTree jgni=null;
        CommonTree n=null;

        try {
            // EsperEPL2Ast.g:586:2: ( ^(jo= EVAL_OR_EXPR valueExpr valueExpr ( valueExpr )* ) | ^(ja= EVAL_AND_EXPR valueExpr valueExpr ( valueExpr )* ) | ^(je= EVAL_EQUALS_EXPR valueExpr valueExpr ) | ^(ji= EVAL_IS_EXPR valueExpr valueExpr ) | ^(jne= EVAL_NOTEQUALS_EXPR valueExpr valueExpr ) | ^(jis= EVAL_ISNOT_EXPR valueExpr valueExpr ) | ^(jge= EVAL_EQUALS_GROUP_EXPR equalsSubquery ) | ^(jgi= EVAL_IS_GROUP_EXPR equalsSubquery ) | ^(jgne= EVAL_NOTEQUALS_GROUP_EXPR equalsSubquery ) | ^(jgni= EVAL_ISNOT_GROUP_EXPR equalsSubquery ) | ^(n= NOT_EXPR valueExpr ) | r= relationalExpr )
            int alt213=12;
            switch ( input.LA(1) ) {
            case EVAL_OR_EXPR:
                {
                alt213=1;
                }
                break;
            case EVAL_AND_EXPR:
                {
                alt213=2;
                }
                break;
            case EVAL_EQUALS_EXPR:
                {
                alt213=3;
                }
                break;
            case EVAL_IS_EXPR:
                {
                alt213=4;
                }
                break;
            case EVAL_NOTEQUALS_EXPR:
                {
                alt213=5;
                }
                break;
            case EVAL_ISNOT_EXPR:
                {
                alt213=6;
                }
                break;
            case EVAL_EQUALS_GROUP_EXPR:
                {
                alt213=7;
                }
                break;
            case EVAL_IS_GROUP_EXPR:
                {
                alt213=8;
                }
                break;
            case EVAL_NOTEQUALS_GROUP_EXPR:
                {
                alt213=9;
                }
                break;
            case EVAL_ISNOT_GROUP_EXPR:
                {
                alt213=10;
                }
                break;
            case NOT_EXPR:
                {
                alt213=11;
                }
                break;
            case LT:
            case GT:
            case LE:
            case GE:
                {
                alt213=12;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 213, 0, input);

                throw nvae;
            }

            switch (alt213) {
                case 1 :
                    // EsperEPL2Ast.g:586:4: ^(jo= EVAL_OR_EXPR valueExpr valueExpr ( valueExpr )* )
                    {
                    jo=(CommonTree)match(input,EVAL_OR_EXPR,FOLLOW_EVAL_OR_EXPR_in_evalExprChoice3664); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3666);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3668);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:586:42: ( valueExpr )*
                    loop211:
                    do {
                        int alt211=2;
                        int LA211_0 = input.LA(1);

                        if ( ((LA211_0>=IN_SET && LA211_0<=REGEXP)||LA211_0==NOT_EXPR||(LA211_0>=SUM && LA211_0<=AVG)||(LA211_0>=COALESCE && LA211_0<=COUNT)||(LA211_0>=CASE && LA211_0<=CASE2)||LA211_0==ISTREAM||(LA211_0>=PREVIOUS && LA211_0<=EXISTS)||(LA211_0>=INSTANCEOF && LA211_0<=CURRENT_TIMESTAMP)||LA211_0==NEWKW||(LA211_0>=EVAL_AND_EXPR && LA211_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA211_0==EVENT_PROP_EXPR||LA211_0==CONCAT||(LA211_0>=LIB_FUNC_CHAIN && LA211_0<=DOT_EXPR)||LA211_0==ARRAY_EXPR||(LA211_0>=NOT_IN_SET && LA211_0<=NOT_REGEXP)||(LA211_0>=IN_RANGE && LA211_0<=SUBSELECT_EXPR)||(LA211_0>=EXISTS_SUBSELECT_EXPR && LA211_0<=NOT_IN_SUBSELECT_EXPR)||LA211_0==SUBSTITUTION||(LA211_0>=FIRST_AGGREG && LA211_0<=WINDOW_AGGREG)||(LA211_0>=INT_TYPE && LA211_0<=NULL_TYPE)||(LA211_0>=JSON_OBJECT && LA211_0<=JSON_ARRAY)||LA211_0==STAR||(LA211_0>=LT && LA211_0<=GT)||(LA211_0>=BOR && LA211_0<=PLUS)||(LA211_0>=BAND && LA211_0<=BXOR)||(LA211_0>=LE && LA211_0<=GE)||(LA211_0>=MINUS && LA211_0<=MOD)||(LA211_0>=EVAL_IS_GROUP_EXPR && LA211_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt211=1;
                        }


                        switch (alt211) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:586:43: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3671);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop211;
                        }
                    } while (true);

                     leaveNode(jo); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:587:4: ^(ja= EVAL_AND_EXPR valueExpr valueExpr ( valueExpr )* )
                    {
                    ja=(CommonTree)match(input,EVAL_AND_EXPR,FOLLOW_EVAL_AND_EXPR_in_evalExprChoice3685); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3687);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3689);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:587:43: ( valueExpr )*
                    loop212:
                    do {
                        int alt212=2;
                        int LA212_0 = input.LA(1);

                        if ( ((LA212_0>=IN_SET && LA212_0<=REGEXP)||LA212_0==NOT_EXPR||(LA212_0>=SUM && LA212_0<=AVG)||(LA212_0>=COALESCE && LA212_0<=COUNT)||(LA212_0>=CASE && LA212_0<=CASE2)||LA212_0==ISTREAM||(LA212_0>=PREVIOUS && LA212_0<=EXISTS)||(LA212_0>=INSTANCEOF && LA212_0<=CURRENT_TIMESTAMP)||LA212_0==NEWKW||(LA212_0>=EVAL_AND_EXPR && LA212_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA212_0==EVENT_PROP_EXPR||LA212_0==CONCAT||(LA212_0>=LIB_FUNC_CHAIN && LA212_0<=DOT_EXPR)||LA212_0==ARRAY_EXPR||(LA212_0>=NOT_IN_SET && LA212_0<=NOT_REGEXP)||(LA212_0>=IN_RANGE && LA212_0<=SUBSELECT_EXPR)||(LA212_0>=EXISTS_SUBSELECT_EXPR && LA212_0<=NOT_IN_SUBSELECT_EXPR)||LA212_0==SUBSTITUTION||(LA212_0>=FIRST_AGGREG && LA212_0<=WINDOW_AGGREG)||(LA212_0>=INT_TYPE && LA212_0<=NULL_TYPE)||(LA212_0>=JSON_OBJECT && LA212_0<=JSON_ARRAY)||LA212_0==STAR||(LA212_0>=LT && LA212_0<=GT)||(LA212_0>=BOR && LA212_0<=PLUS)||(LA212_0>=BAND && LA212_0<=BXOR)||(LA212_0>=LE && LA212_0<=GE)||(LA212_0>=MINUS && LA212_0<=MOD)||(LA212_0>=EVAL_IS_GROUP_EXPR && LA212_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt212=1;
                        }


                        switch (alt212) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:587:44: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3692);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop212;
                        }
                    } while (true);

                     leaveNode(ja); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:588:4: ^(je= EVAL_EQUALS_EXPR valueExpr valueExpr )
                    {
                    je=(CommonTree)match(input,EVAL_EQUALS_EXPR,FOLLOW_EVAL_EQUALS_EXPR_in_evalExprChoice3706); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3708);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3710);
                    valueExpr();

                    state._fsp--;

                     leaveNode(je); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:589:4: ^(ji= EVAL_IS_EXPR valueExpr valueExpr )
                    {
                    ji=(CommonTree)match(input,EVAL_IS_EXPR,FOLLOW_EVAL_IS_EXPR_in_evalExprChoice3722); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3724);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3726);
                    valueExpr();

                    state._fsp--;

                     leaveNode(ji); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:590:4: ^(jne= EVAL_NOTEQUALS_EXPR valueExpr valueExpr )
                    {
                    jne=(CommonTree)match(input,EVAL_NOTEQUALS_EXPR,FOLLOW_EVAL_NOTEQUALS_EXPR_in_evalExprChoice3738); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3740);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3742);
                    valueExpr();

                    state._fsp--;

                     leaveNode(jne); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:591:4: ^(jis= EVAL_ISNOT_EXPR valueExpr valueExpr )
                    {
                    jis=(CommonTree)match(input,EVAL_ISNOT_EXPR,FOLLOW_EVAL_ISNOT_EXPR_in_evalExprChoice3754); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3756);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3758);
                    valueExpr();

                    state._fsp--;

                     leaveNode(jis); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:592:4: ^(jge= EVAL_EQUALS_GROUP_EXPR equalsSubquery )
                    {
                    jge=(CommonTree)match(input,EVAL_EQUALS_GROUP_EXPR,FOLLOW_EVAL_EQUALS_GROUP_EXPR_in_evalExprChoice3770); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_equalsSubquery_in_evalExprChoice3772);
                    equalsSubquery();

                    state._fsp--;

                     leaveNode(jge); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 8 :
                    // EsperEPL2Ast.g:593:4: ^(jgi= EVAL_IS_GROUP_EXPR equalsSubquery )
                    {
                    jgi=(CommonTree)match(input,EVAL_IS_GROUP_EXPR,FOLLOW_EVAL_IS_GROUP_EXPR_in_evalExprChoice3784); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_equalsSubquery_in_evalExprChoice3786);
                    equalsSubquery();

                    state._fsp--;

                     leaveNode(jgi); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 9 :
                    // EsperEPL2Ast.g:594:4: ^(jgne= EVAL_NOTEQUALS_GROUP_EXPR equalsSubquery )
                    {
                    jgne=(CommonTree)match(input,EVAL_NOTEQUALS_GROUP_EXPR,FOLLOW_EVAL_NOTEQUALS_GROUP_EXPR_in_evalExprChoice3798); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_equalsSubquery_in_evalExprChoice3800);
                    equalsSubquery();

                    state._fsp--;

                     leaveNode(jgne); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 10 :
                    // EsperEPL2Ast.g:595:4: ^(jgni= EVAL_ISNOT_GROUP_EXPR equalsSubquery )
                    {
                    jgni=(CommonTree)match(input,EVAL_ISNOT_GROUP_EXPR,FOLLOW_EVAL_ISNOT_GROUP_EXPR_in_evalExprChoice3812); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_equalsSubquery_in_evalExprChoice3814);
                    equalsSubquery();

                    state._fsp--;

                     leaveNode(jgni); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 11 :
                    // EsperEPL2Ast.g:596:4: ^(n= NOT_EXPR valueExpr )
                    {
                    n=(CommonTree)match(input,NOT_EXPR,FOLLOW_NOT_EXPR_in_evalExprChoice3826); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_evalExprChoice3828);
                    valueExpr();

                    state._fsp--;

                     leaveNode(n); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 12 :
                    // EsperEPL2Ast.g:597:4: r= relationalExpr
                    {
                    pushFollow(FOLLOW_relationalExpr_in_evalExprChoice3839);
                    relationalExpr();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "evalExprChoice"


    // $ANTLR start "equalsSubquery"
    // EsperEPL2Ast.g:600:1: equalsSubquery : valueExpr ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) ;
    public final void equalsSubquery() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:601:2: ( valueExpr ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr ) )
            // EsperEPL2Ast.g:601:4: valueExpr ( ANY | SOME | ALL ) ( ( valueExpr )* | subSelectGroupExpr )
            {
            pushFollow(FOLLOW_valueExpr_in_equalsSubquery3850);
            valueExpr();

            state._fsp--;

            if ( (input.LA(1)>=ALL && input.LA(1)<=SOME) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            // EsperEPL2Ast.g:601:29: ( ( valueExpr )* | subSelectGroupExpr )
            int alt215=2;
            int LA215_0 = input.LA(1);

            if ( (LA215_0==UP||(LA215_0>=IN_SET && LA215_0<=REGEXP)||LA215_0==NOT_EXPR||(LA215_0>=SUM && LA215_0<=AVG)||(LA215_0>=COALESCE && LA215_0<=COUNT)||(LA215_0>=CASE && LA215_0<=CASE2)||LA215_0==ISTREAM||(LA215_0>=PREVIOUS && LA215_0<=EXISTS)||(LA215_0>=INSTANCEOF && LA215_0<=CURRENT_TIMESTAMP)||LA215_0==NEWKW||(LA215_0>=EVAL_AND_EXPR && LA215_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA215_0==EVENT_PROP_EXPR||LA215_0==CONCAT||(LA215_0>=LIB_FUNC_CHAIN && LA215_0<=DOT_EXPR)||LA215_0==ARRAY_EXPR||(LA215_0>=NOT_IN_SET && LA215_0<=NOT_REGEXP)||(LA215_0>=IN_RANGE && LA215_0<=SUBSELECT_EXPR)||(LA215_0>=EXISTS_SUBSELECT_EXPR && LA215_0<=NOT_IN_SUBSELECT_EXPR)||LA215_0==SUBSTITUTION||(LA215_0>=FIRST_AGGREG && LA215_0<=WINDOW_AGGREG)||(LA215_0>=INT_TYPE && LA215_0<=NULL_TYPE)||(LA215_0>=JSON_OBJECT && LA215_0<=JSON_ARRAY)||LA215_0==STAR||(LA215_0>=LT && LA215_0<=GT)||(LA215_0>=BOR && LA215_0<=PLUS)||(LA215_0>=BAND && LA215_0<=BXOR)||(LA215_0>=LE && LA215_0<=GE)||(LA215_0>=MINUS && LA215_0<=MOD)||(LA215_0>=EVAL_IS_GROUP_EXPR && LA215_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt215=1;
            }
            else if ( (LA215_0==SUBSELECT_GROUP_EXPR) ) {
                alt215=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 215, 0, input);

                throw nvae;
            }
            switch (alt215) {
                case 1 :
                    // EsperEPL2Ast.g:601:30: ( valueExpr )*
                    {
                    // EsperEPL2Ast.g:601:30: ( valueExpr )*
                    loop214:
                    do {
                        int alt214=2;
                        int LA214_0 = input.LA(1);

                        if ( ((LA214_0>=IN_SET && LA214_0<=REGEXP)||LA214_0==NOT_EXPR||(LA214_0>=SUM && LA214_0<=AVG)||(LA214_0>=COALESCE && LA214_0<=COUNT)||(LA214_0>=CASE && LA214_0<=CASE2)||LA214_0==ISTREAM||(LA214_0>=PREVIOUS && LA214_0<=EXISTS)||(LA214_0>=INSTANCEOF && LA214_0<=CURRENT_TIMESTAMP)||LA214_0==NEWKW||(LA214_0>=EVAL_AND_EXPR && LA214_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA214_0==EVENT_PROP_EXPR||LA214_0==CONCAT||(LA214_0>=LIB_FUNC_CHAIN && LA214_0<=DOT_EXPR)||LA214_0==ARRAY_EXPR||(LA214_0>=NOT_IN_SET && LA214_0<=NOT_REGEXP)||(LA214_0>=IN_RANGE && LA214_0<=SUBSELECT_EXPR)||(LA214_0>=EXISTS_SUBSELECT_EXPR && LA214_0<=NOT_IN_SUBSELECT_EXPR)||LA214_0==SUBSTITUTION||(LA214_0>=FIRST_AGGREG && LA214_0<=WINDOW_AGGREG)||(LA214_0>=INT_TYPE && LA214_0<=NULL_TYPE)||(LA214_0>=JSON_OBJECT && LA214_0<=JSON_ARRAY)||LA214_0==STAR||(LA214_0>=LT && LA214_0<=GT)||(LA214_0>=BOR && LA214_0<=PLUS)||(LA214_0>=BAND && LA214_0<=BXOR)||(LA214_0>=LE && LA214_0<=GE)||(LA214_0>=MINUS && LA214_0<=MOD)||(LA214_0>=EVAL_IS_GROUP_EXPR && LA214_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt214=1;
                        }


                        switch (alt214) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:601:30: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_equalsSubquery3861);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop214;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:601:43: subSelectGroupExpr
                    {
                    pushFollow(FOLLOW_subSelectGroupExpr_in_equalsSubquery3866);
                    subSelectGroupExpr();

                    state._fsp--;


                    }
                    break;

            }


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "equalsSubquery"


    // $ANTLR start "valueExpr"
    // EsperEPL2Ast.g:604:1: valueExpr : ( constant[true] | substitution | arithmeticExpr | eventPropertyExpr[true] | evalExprChoice | builtinFunc | libFuncChain | caseExpr | inExpr | betweenExpr | likeExpr | regExpExpr | arrayExpr | subSelectInExpr | subSelectRowExpr | subSelectExistsExpr | dotExpr | newExpr | jsonarray[true] | jsonobject[true] );
    public final void valueExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:605:2: ( constant[true] | substitution | arithmeticExpr | eventPropertyExpr[true] | evalExprChoice | builtinFunc | libFuncChain | caseExpr | inExpr | betweenExpr | likeExpr | regExpExpr | arrayExpr | subSelectInExpr | subSelectRowExpr | subSelectExistsExpr | dotExpr | newExpr | jsonarray[true] | jsonobject[true] )
            int alt216=20;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt216=1;
                }
                break;
            case SUBSTITUTION:
                {
                alt216=2;
                }
                break;
            case CONCAT:
            case STAR:
            case BOR:
            case PLUS:
            case BAND:
            case BXOR:
            case MINUS:
            case DIV:
            case MOD:
                {
                alt216=3;
                }
                break;
            case EVENT_PROP_EXPR:
                {
                alt216=4;
                }
                break;
            case NOT_EXPR:
            case EVAL_AND_EXPR:
            case EVAL_OR_EXPR:
            case EVAL_EQUALS_EXPR:
            case EVAL_NOTEQUALS_EXPR:
            case EVAL_IS_EXPR:
            case EVAL_ISNOT_EXPR:
            case EVAL_EQUALS_GROUP_EXPR:
            case EVAL_NOTEQUALS_GROUP_EXPR:
            case LT:
            case GT:
            case LE:
            case GE:
            case EVAL_IS_GROUP_EXPR:
            case EVAL_ISNOT_GROUP_EXPR:
                {
                alt216=5;
                }
                break;
            case SUM:
            case AVG:
            case COALESCE:
            case MEDIAN:
            case STDDEV:
            case AVEDEV:
            case COUNT:
            case ISTREAM:
            case PREVIOUS:
            case PREVIOUSTAIL:
            case PREVIOUSCOUNT:
            case PREVIOUSWINDOW:
            case PRIOR:
            case EXISTS:
            case INSTANCEOF:
            case TYPEOF:
            case CAST:
            case CURRENT_TIMESTAMP:
            case FIRST_AGGREG:
            case LAST_AGGREG:
            case WINDOW_AGGREG:
                {
                alt216=6;
                }
                break;
            case LIB_FUNC_CHAIN:
                {
                alt216=7;
                }
                break;
            case CASE:
            case CASE2:
                {
                alt216=8;
                }
                break;
            case IN_SET:
            case NOT_IN_SET:
            case IN_RANGE:
            case NOT_IN_RANGE:
                {
                alt216=9;
                }
                break;
            case BETWEEN:
            case NOT_BETWEEN:
                {
                alt216=10;
                }
                break;
            case LIKE:
            case NOT_LIKE:
                {
                alt216=11;
                }
                break;
            case REGEXP:
            case NOT_REGEXP:
                {
                alt216=12;
                }
                break;
            case ARRAY_EXPR:
                {
                alt216=13;
                }
                break;
            case IN_SUBSELECT_EXPR:
            case NOT_IN_SUBSELECT_EXPR:
                {
                alt216=14;
                }
                break;
            case SUBSELECT_EXPR:
                {
                alt216=15;
                }
                break;
            case EXISTS_SUBSELECT_EXPR:
                {
                alt216=16;
                }
                break;
            case DOT_EXPR:
                {
                alt216=17;
                }
                break;
            case NEWKW:
                {
                alt216=18;
                }
                break;
            case JSON_ARRAY:
                {
                alt216=19;
                }
                break;
            case JSON_OBJECT:
                {
                alt216=20;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 216, 0, input);

                throw nvae;
            }

            switch (alt216) {
                case 1 :
                    // EsperEPL2Ast.g:605:5: constant[true]
                    {
                    pushFollow(FOLLOW_constant_in_valueExpr3880);
                    constant(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:606:4: substitution
                    {
                    pushFollow(FOLLOW_substitution_in_valueExpr3886);
                    substitution();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:607:5: arithmeticExpr
                    {
                    pushFollow(FOLLOW_arithmeticExpr_in_valueExpr3892);
                    arithmeticExpr();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:608:5: eventPropertyExpr[true]
                    {
                    pushFollow(FOLLOW_eventPropertyExpr_in_valueExpr3899);
                    eventPropertyExpr(true);

                    state._fsp--;


                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:609:7: evalExprChoice
                    {
                    pushFollow(FOLLOW_evalExprChoice_in_valueExpr3908);
                    evalExprChoice();

                    state._fsp--;


                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:610:4: builtinFunc
                    {
                    pushFollow(FOLLOW_builtinFunc_in_valueExpr3913);
                    builtinFunc();

                    state._fsp--;


                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:611:7: libFuncChain
                    {
                    pushFollow(FOLLOW_libFuncChain_in_valueExpr3921);
                    libFuncChain();

                    state._fsp--;


                    }
                    break;
                case 8 :
                    // EsperEPL2Ast.g:612:4: caseExpr
                    {
                    pushFollow(FOLLOW_caseExpr_in_valueExpr3926);
                    caseExpr();

                    state._fsp--;


                    }
                    break;
                case 9 :
                    // EsperEPL2Ast.g:613:4: inExpr
                    {
                    pushFollow(FOLLOW_inExpr_in_valueExpr3931);
                    inExpr();

                    state._fsp--;


                    }
                    break;
                case 10 :
                    // EsperEPL2Ast.g:614:4: betweenExpr
                    {
                    pushFollow(FOLLOW_betweenExpr_in_valueExpr3937);
                    betweenExpr();

                    state._fsp--;


                    }
                    break;
                case 11 :
                    // EsperEPL2Ast.g:615:4: likeExpr
                    {
                    pushFollow(FOLLOW_likeExpr_in_valueExpr3942);
                    likeExpr();

                    state._fsp--;


                    }
                    break;
                case 12 :
                    // EsperEPL2Ast.g:616:4: regExpExpr
                    {
                    pushFollow(FOLLOW_regExpExpr_in_valueExpr3947);
                    regExpExpr();

                    state._fsp--;


                    }
                    break;
                case 13 :
                    // EsperEPL2Ast.g:617:4: arrayExpr
                    {
                    pushFollow(FOLLOW_arrayExpr_in_valueExpr3952);
                    arrayExpr();

                    state._fsp--;


                    }
                    break;
                case 14 :
                    // EsperEPL2Ast.g:618:4: subSelectInExpr
                    {
                    pushFollow(FOLLOW_subSelectInExpr_in_valueExpr3957);
                    subSelectInExpr();

                    state._fsp--;


                    }
                    break;
                case 15 :
                    // EsperEPL2Ast.g:619:5: subSelectRowExpr
                    {
                    pushFollow(FOLLOW_subSelectRowExpr_in_valueExpr3963);
                    subSelectRowExpr();

                    state._fsp--;


                    }
                    break;
                case 16 :
                    // EsperEPL2Ast.g:620:5: subSelectExistsExpr
                    {
                    pushFollow(FOLLOW_subSelectExistsExpr_in_valueExpr3970);
                    subSelectExistsExpr();

                    state._fsp--;


                    }
                    break;
                case 17 :
                    // EsperEPL2Ast.g:621:4: dotExpr
                    {
                    pushFollow(FOLLOW_dotExpr_in_valueExpr3975);
                    dotExpr();

                    state._fsp--;


                    }
                    break;
                case 18 :
                    // EsperEPL2Ast.g:622:4: newExpr
                    {
                    pushFollow(FOLLOW_newExpr_in_valueExpr3980);
                    newExpr();

                    state._fsp--;


                    }
                    break;
                case 19 :
                    // EsperEPL2Ast.g:623:4: jsonarray[true]
                    {
                    pushFollow(FOLLOW_jsonarray_in_valueExpr3985);
                    jsonarray(true);

                    state._fsp--;


                    }
                    break;
                case 20 :
                    // EsperEPL2Ast.g:624:4: jsonobject[true]
                    {
                    pushFollow(FOLLOW_jsonobject_in_valueExpr3991);
                    jsonobject(true);

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "valueExpr"


    // $ANTLR start "valueExprWithTime"
    // EsperEPL2Ast.g:627:1: valueExprWithTime : (l= LAST | lw= LW | valueExpr | ^(ordered= OBJECT_PARAM_ORDERED_EXPR valueExpr ( DESC | ASC ) ) | rangeOperator | frequencyOperator | lastOperator | weekDayOperator | ^(l= NUMERIC_PARAM_LIST ( numericParameterList )+ ) | s= NUMBERSETSTAR | timePeriod );
    public final void valueExprWithTime() throws RecognitionException {
        CommonTree l=null;
        CommonTree lw=null;
        CommonTree ordered=null;
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:628:2: (l= LAST | lw= LW | valueExpr | ^(ordered= OBJECT_PARAM_ORDERED_EXPR valueExpr ( DESC | ASC ) ) | rangeOperator | frequencyOperator | lastOperator | weekDayOperator | ^(l= NUMERIC_PARAM_LIST ( numericParameterList )+ ) | s= NUMBERSETSTAR | timePeriod )
            int alt218=11;
            switch ( input.LA(1) ) {
            case LAST:
                {
                alt218=1;
                }
                break;
            case LW:
                {
                alt218=2;
                }
                break;
            case IN_SET:
            case BETWEEN:
            case LIKE:
            case REGEXP:
            case NOT_EXPR:
            case SUM:
            case AVG:
            case COALESCE:
            case MEDIAN:
            case STDDEV:
            case AVEDEV:
            case COUNT:
            case CASE:
            case CASE2:
            case ISTREAM:
            case PREVIOUS:
            case PREVIOUSTAIL:
            case PREVIOUSCOUNT:
            case PREVIOUSWINDOW:
            case PRIOR:
            case EXISTS:
            case INSTANCEOF:
            case TYPEOF:
            case CAST:
            case CURRENT_TIMESTAMP:
            case NEWKW:
            case EVAL_AND_EXPR:
            case EVAL_OR_EXPR:
            case EVAL_EQUALS_EXPR:
            case EVAL_NOTEQUALS_EXPR:
            case EVAL_IS_EXPR:
            case EVAL_ISNOT_EXPR:
            case EVAL_EQUALS_GROUP_EXPR:
            case EVAL_NOTEQUALS_GROUP_EXPR:
            case EVENT_PROP_EXPR:
            case CONCAT:
            case LIB_FUNC_CHAIN:
            case DOT_EXPR:
            case ARRAY_EXPR:
            case NOT_IN_SET:
            case NOT_BETWEEN:
            case NOT_LIKE:
            case NOT_REGEXP:
            case IN_RANGE:
            case NOT_IN_RANGE:
            case SUBSELECT_EXPR:
            case EXISTS_SUBSELECT_EXPR:
            case IN_SUBSELECT_EXPR:
            case NOT_IN_SUBSELECT_EXPR:
            case SUBSTITUTION:
            case FIRST_AGGREG:
            case LAST_AGGREG:
            case WINDOW_AGGREG:
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
            case JSON_OBJECT:
            case JSON_ARRAY:
            case STAR:
            case LT:
            case GT:
            case BOR:
            case PLUS:
            case BAND:
            case BXOR:
            case LE:
            case GE:
            case MINUS:
            case DIV:
            case MOD:
            case EVAL_IS_GROUP_EXPR:
            case EVAL_ISNOT_GROUP_EXPR:
                {
                alt218=3;
                }
                break;
            case OBJECT_PARAM_ORDERED_EXPR:
                {
                alt218=4;
                }
                break;
            case NUMERIC_PARAM_RANGE:
                {
                alt218=5;
                }
                break;
            case NUMERIC_PARAM_FREQUENCY:
                {
                alt218=6;
                }
                break;
            case LAST_OPERATOR:
                {
                alt218=7;
                }
                break;
            case WEEKDAY_OPERATOR:
                {
                alt218=8;
                }
                break;
            case NUMERIC_PARAM_LIST:
                {
                alt218=9;
                }
                break;
            case NUMBERSETSTAR:
                {
                alt218=10;
                }
                break;
            case TIME_PERIOD:
                {
                alt218=11;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 218, 0, input);

                throw nvae;
            }

            switch (alt218) {
                case 1 :
                    // EsperEPL2Ast.g:628:4: l= LAST
                    {
                    l=(CommonTree)match(input,LAST,FOLLOW_LAST_in_valueExprWithTime4005); 
                     leaveNode(l); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:629:4: lw= LW
                    {
                    lw=(CommonTree)match(input,LW,FOLLOW_LW_in_valueExprWithTime4014); 
                     leaveNode(lw); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:630:4: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_valueExprWithTime4021);
                    valueExpr();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:631:4: ^(ordered= OBJECT_PARAM_ORDERED_EXPR valueExpr ( DESC | ASC ) )
                    {
                    ordered=(CommonTree)match(input,OBJECT_PARAM_ORDERED_EXPR,FOLLOW_OBJECT_PARAM_ORDERED_EXPR_in_valueExprWithTime4029); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_valueExprWithTime4031);
                    valueExpr();

                    state._fsp--;

                    if ( (input.LA(1)>=ASC && input.LA(1)<=DESC) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                     leaveNode(ordered); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:632:5: rangeOperator
                    {
                    pushFollow(FOLLOW_rangeOperator_in_valueExprWithTime4046);
                    rangeOperator();

                    state._fsp--;


                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:633:5: frequencyOperator
                    {
                    pushFollow(FOLLOW_frequencyOperator_in_valueExprWithTime4052);
                    frequencyOperator();

                    state._fsp--;


                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:634:4: lastOperator
                    {
                    pushFollow(FOLLOW_lastOperator_in_valueExprWithTime4057);
                    lastOperator();

                    state._fsp--;


                    }
                    break;
                case 8 :
                    // EsperEPL2Ast.g:635:4: weekDayOperator
                    {
                    pushFollow(FOLLOW_weekDayOperator_in_valueExprWithTime4062);
                    weekDayOperator();

                    state._fsp--;


                    }
                    break;
                case 9 :
                    // EsperEPL2Ast.g:636:5: ^(l= NUMERIC_PARAM_LIST ( numericParameterList )+ )
                    {
                    l=(CommonTree)match(input,NUMERIC_PARAM_LIST,FOLLOW_NUMERIC_PARAM_LIST_in_valueExprWithTime4072); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:636:29: ( numericParameterList )+
                    int cnt217=0;
                    loop217:
                    do {
                        int alt217=2;
                        int LA217_0 = input.LA(1);

                        if ( (LA217_0==NUMERIC_PARAM_RANGE||LA217_0==NUMERIC_PARAM_FREQUENCY||(LA217_0>=INT_TYPE && LA217_0<=NULL_TYPE)) ) {
                            alt217=1;
                        }


                        switch (alt217) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:636:29: numericParameterList
                    	    {
                    	    pushFollow(FOLLOW_numericParameterList_in_valueExprWithTime4074);
                    	    numericParameterList();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt217 >= 1 ) break loop217;
                                EarlyExitException eee =
                                    new EarlyExitException(217, input);
                                throw eee;
                        }
                        cnt217++;
                    } while (true);

                     leaveNode(l); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 10 :
                    // EsperEPL2Ast.g:637:4: s= NUMBERSETSTAR
                    {
                    s=(CommonTree)match(input,NUMBERSETSTAR,FOLLOW_NUMBERSETSTAR_in_valueExprWithTime4085); 
                     leaveNode(s); 

                    }
                    break;
                case 11 :
                    // EsperEPL2Ast.g:638:4: timePeriod
                    {
                    pushFollow(FOLLOW_timePeriod_in_valueExprWithTime4092);
                    timePeriod();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "valueExprWithTime"


    // $ANTLR start "numericParameterList"
    // EsperEPL2Ast.g:641:1: numericParameterList : ( constant[true] | rangeOperator | frequencyOperator );
    public final void numericParameterList() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:642:2: ( constant[true] | rangeOperator | frequencyOperator )
            int alt219=3;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt219=1;
                }
                break;
            case NUMERIC_PARAM_RANGE:
                {
                alt219=2;
                }
                break;
            case NUMERIC_PARAM_FREQUENCY:
                {
                alt219=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 219, 0, input);

                throw nvae;
            }

            switch (alt219) {
                case 1 :
                    // EsperEPL2Ast.g:642:5: constant[true]
                    {
                    pushFollow(FOLLOW_constant_in_numericParameterList4105);
                    constant(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:643:5: rangeOperator
                    {
                    pushFollow(FOLLOW_rangeOperator_in_numericParameterList4112);
                    rangeOperator();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:644:5: frequencyOperator
                    {
                    pushFollow(FOLLOW_frequencyOperator_in_numericParameterList4118);
                    frequencyOperator();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "numericParameterList"


    // $ANTLR start "rangeOperator"
    // EsperEPL2Ast.g:647:1: rangeOperator : ^(r= NUMERIC_PARAM_RANGE ( constant[true] | eventPropertyExpr[true] | substitution ) ( constant[true] | eventPropertyExpr[true] | substitution ) ) ;
    public final void rangeOperator() throws RecognitionException {
        CommonTree r=null;

        try {
            // EsperEPL2Ast.g:648:2: ( ^(r= NUMERIC_PARAM_RANGE ( constant[true] | eventPropertyExpr[true] | substitution ) ( constant[true] | eventPropertyExpr[true] | substitution ) ) )
            // EsperEPL2Ast.g:648:4: ^(r= NUMERIC_PARAM_RANGE ( constant[true] | eventPropertyExpr[true] | substitution ) ( constant[true] | eventPropertyExpr[true] | substitution ) )
            {
            r=(CommonTree)match(input,NUMERIC_PARAM_RANGE,FOLLOW_NUMERIC_PARAM_RANGE_in_rangeOperator4134); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:648:29: ( constant[true] | eventPropertyExpr[true] | substitution )
            int alt220=3;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt220=1;
                }
                break;
            case EVENT_PROP_EXPR:
                {
                alt220=2;
                }
                break;
            case SUBSTITUTION:
                {
                alt220=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 220, 0, input);

                throw nvae;
            }

            switch (alt220) {
                case 1 :
                    // EsperEPL2Ast.g:648:30: constant[true]
                    {
                    pushFollow(FOLLOW_constant_in_rangeOperator4137);
                    constant(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:648:45: eventPropertyExpr[true]
                    {
                    pushFollow(FOLLOW_eventPropertyExpr_in_rangeOperator4140);
                    eventPropertyExpr(true);

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:648:69: substitution
                    {
                    pushFollow(FOLLOW_substitution_in_rangeOperator4143);
                    substitution();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:648:83: ( constant[true] | eventPropertyExpr[true] | substitution )
            int alt221=3;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt221=1;
                }
                break;
            case EVENT_PROP_EXPR:
                {
                alt221=2;
                }
                break;
            case SUBSTITUTION:
                {
                alt221=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 221, 0, input);

                throw nvae;
            }

            switch (alt221) {
                case 1 :
                    // EsperEPL2Ast.g:648:84: constant[true]
                    {
                    pushFollow(FOLLOW_constant_in_rangeOperator4147);
                    constant(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:648:99: eventPropertyExpr[true]
                    {
                    pushFollow(FOLLOW_eventPropertyExpr_in_rangeOperator4150);
                    eventPropertyExpr(true);

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:648:123: substitution
                    {
                    pushFollow(FOLLOW_substitution_in_rangeOperator4153);
                    substitution();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(r); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "rangeOperator"


    // $ANTLR start "frequencyOperator"
    // EsperEPL2Ast.g:651:1: frequencyOperator : ^(f= NUMERIC_PARAM_FREQUENCY ( constant[true] | eventPropertyExpr[true] | substitution ) ) ;
    public final void frequencyOperator() throws RecognitionException {
        CommonTree f=null;

        try {
            // EsperEPL2Ast.g:652:2: ( ^(f= NUMERIC_PARAM_FREQUENCY ( constant[true] | eventPropertyExpr[true] | substitution ) ) )
            // EsperEPL2Ast.g:652:4: ^(f= NUMERIC_PARAM_FREQUENCY ( constant[true] | eventPropertyExpr[true] | substitution ) )
            {
            f=(CommonTree)match(input,NUMERIC_PARAM_FREQUENCY,FOLLOW_NUMERIC_PARAM_FREQUENCY_in_frequencyOperator4174); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:652:33: ( constant[true] | eventPropertyExpr[true] | substitution )
            int alt222=3;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt222=1;
                }
                break;
            case EVENT_PROP_EXPR:
                {
                alt222=2;
                }
                break;
            case SUBSTITUTION:
                {
                alt222=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 222, 0, input);

                throw nvae;
            }

            switch (alt222) {
                case 1 :
                    // EsperEPL2Ast.g:652:34: constant[true]
                    {
                    pushFollow(FOLLOW_constant_in_frequencyOperator4177);
                    constant(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:652:49: eventPropertyExpr[true]
                    {
                    pushFollow(FOLLOW_eventPropertyExpr_in_frequencyOperator4180);
                    eventPropertyExpr(true);

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:652:73: substitution
                    {
                    pushFollow(FOLLOW_substitution_in_frequencyOperator4183);
                    substitution();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(f); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "frequencyOperator"


    // $ANTLR start "lastOperator"
    // EsperEPL2Ast.g:655:1: lastOperator : ^(l= LAST_OPERATOR ( constant[true] | eventPropertyExpr[true] | substitution ) ) ;
    public final void lastOperator() throws RecognitionException {
        CommonTree l=null;

        try {
            // EsperEPL2Ast.g:656:2: ( ^(l= LAST_OPERATOR ( constant[true] | eventPropertyExpr[true] | substitution ) ) )
            // EsperEPL2Ast.g:656:4: ^(l= LAST_OPERATOR ( constant[true] | eventPropertyExpr[true] | substitution ) )
            {
            l=(CommonTree)match(input,LAST_OPERATOR,FOLLOW_LAST_OPERATOR_in_lastOperator4202); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:656:23: ( constant[true] | eventPropertyExpr[true] | substitution )
            int alt223=3;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt223=1;
                }
                break;
            case EVENT_PROP_EXPR:
                {
                alt223=2;
                }
                break;
            case SUBSTITUTION:
                {
                alt223=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 223, 0, input);

                throw nvae;
            }

            switch (alt223) {
                case 1 :
                    // EsperEPL2Ast.g:656:24: constant[true]
                    {
                    pushFollow(FOLLOW_constant_in_lastOperator4205);
                    constant(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:656:39: eventPropertyExpr[true]
                    {
                    pushFollow(FOLLOW_eventPropertyExpr_in_lastOperator4208);
                    eventPropertyExpr(true);

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:656:63: substitution
                    {
                    pushFollow(FOLLOW_substitution_in_lastOperator4211);
                    substitution();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(l); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "lastOperator"


    // $ANTLR start "weekDayOperator"
    // EsperEPL2Ast.g:659:1: weekDayOperator : ^(w= WEEKDAY_OPERATOR ( constant[true] | eventPropertyExpr[true] | substitution ) ) ;
    public final void weekDayOperator() throws RecognitionException {
        CommonTree w=null;

        try {
            // EsperEPL2Ast.g:660:2: ( ^(w= WEEKDAY_OPERATOR ( constant[true] | eventPropertyExpr[true] | substitution ) ) )
            // EsperEPL2Ast.g:660:4: ^(w= WEEKDAY_OPERATOR ( constant[true] | eventPropertyExpr[true] | substitution ) )
            {
            w=(CommonTree)match(input,WEEKDAY_OPERATOR,FOLLOW_WEEKDAY_OPERATOR_in_weekDayOperator4230); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:660:26: ( constant[true] | eventPropertyExpr[true] | substitution )
            int alt224=3;
            switch ( input.LA(1) ) {
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
                {
                alt224=1;
                }
                break;
            case EVENT_PROP_EXPR:
                {
                alt224=2;
                }
                break;
            case SUBSTITUTION:
                {
                alt224=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 224, 0, input);

                throw nvae;
            }

            switch (alt224) {
                case 1 :
                    // EsperEPL2Ast.g:660:27: constant[true]
                    {
                    pushFollow(FOLLOW_constant_in_weekDayOperator4233);
                    constant(true);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:660:42: eventPropertyExpr[true]
                    {
                    pushFollow(FOLLOW_eventPropertyExpr_in_weekDayOperator4236);
                    eventPropertyExpr(true);

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:660:66: substitution
                    {
                    pushFollow(FOLLOW_substitution_in_weekDayOperator4239);
                    substitution();

                    state._fsp--;


                    }
                    break;

            }

             leaveNode(w); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "weekDayOperator"


    // $ANTLR start "subSelectGroupExpr"
    // EsperEPL2Ast.g:663:1: subSelectGroupExpr : ^(s= SUBSELECT_GROUP_EXPR subQueryExpr ) ;
    public final void subSelectGroupExpr() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:664:2: ( ^(s= SUBSELECT_GROUP_EXPR subQueryExpr ) )
            // EsperEPL2Ast.g:664:4: ^(s= SUBSELECT_GROUP_EXPR subQueryExpr )
            {
            pushStmtContext();
            s=(CommonTree)match(input,SUBSELECT_GROUP_EXPR,FOLLOW_SUBSELECT_GROUP_EXPR_in_subSelectGroupExpr4260); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_subQueryExpr_in_subSelectGroupExpr4262);
            subQueryExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "subSelectGroupExpr"


    // $ANTLR start "subSelectRowExpr"
    // EsperEPL2Ast.g:667:1: subSelectRowExpr : ^(s= SUBSELECT_EXPR subQueryExpr ) ;
    public final void subSelectRowExpr() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:668:2: ( ^(s= SUBSELECT_EXPR subQueryExpr ) )
            // EsperEPL2Ast.g:668:4: ^(s= SUBSELECT_EXPR subQueryExpr )
            {
            pushStmtContext();
            s=(CommonTree)match(input,SUBSELECT_EXPR,FOLLOW_SUBSELECT_EXPR_in_subSelectRowExpr4281); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_subQueryExpr_in_subSelectRowExpr4283);
            subQueryExpr();

            state._fsp--;


            match(input, Token.UP, null); 
            leaveNode(s);

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "subSelectRowExpr"


    // $ANTLR start "subSelectExistsExpr"
    // EsperEPL2Ast.g:671:1: subSelectExistsExpr : ^(e= EXISTS_SUBSELECT_EXPR subQueryExpr ) ;
    public final void subSelectExistsExpr() throws RecognitionException {
        CommonTree e=null;

        try {
            // EsperEPL2Ast.g:672:2: ( ^(e= EXISTS_SUBSELECT_EXPR subQueryExpr ) )
            // EsperEPL2Ast.g:672:4: ^(e= EXISTS_SUBSELECT_EXPR subQueryExpr )
            {
            pushStmtContext();
            e=(CommonTree)match(input,EXISTS_SUBSELECT_EXPR,FOLLOW_EXISTS_SUBSELECT_EXPR_in_subSelectExistsExpr4302); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_subQueryExpr_in_subSelectExistsExpr4304);
            subQueryExpr();

            state._fsp--;


            match(input, Token.UP, null); 
            leaveNode(e);

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "subSelectExistsExpr"


    // $ANTLR start "subSelectInExpr"
    // EsperEPL2Ast.g:675:1: subSelectInExpr : ( ^(s= IN_SUBSELECT_EXPR valueExpr subSelectInQueryExpr ) | ^(s= NOT_IN_SUBSELECT_EXPR valueExpr subSelectInQueryExpr ) );
    public final void subSelectInExpr() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:676:2: ( ^(s= IN_SUBSELECT_EXPR valueExpr subSelectInQueryExpr ) | ^(s= NOT_IN_SUBSELECT_EXPR valueExpr subSelectInQueryExpr ) )
            int alt225=2;
            int LA225_0 = input.LA(1);

            if ( (LA225_0==IN_SUBSELECT_EXPR) ) {
                alt225=1;
            }
            else if ( (LA225_0==NOT_IN_SUBSELECT_EXPR) ) {
                alt225=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 225, 0, input);

                throw nvae;
            }
            switch (alt225) {
                case 1 :
                    // EsperEPL2Ast.g:676:5: ^(s= IN_SUBSELECT_EXPR valueExpr subSelectInQueryExpr )
                    {
                    s=(CommonTree)match(input,IN_SUBSELECT_EXPR,FOLLOW_IN_SUBSELECT_EXPR_in_subSelectInExpr4323); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_subSelectInExpr4325);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_subSelectInQueryExpr_in_subSelectInExpr4327);
                    subSelectInQueryExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(s); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:677:5: ^(s= NOT_IN_SUBSELECT_EXPR valueExpr subSelectInQueryExpr )
                    {
                    s=(CommonTree)match(input,NOT_IN_SUBSELECT_EXPR,FOLLOW_NOT_IN_SUBSELECT_EXPR_in_subSelectInExpr4339); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_subSelectInExpr4341);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_subSelectInQueryExpr_in_subSelectInExpr4343);
                    subSelectInQueryExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(s); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "subSelectInExpr"


    // $ANTLR start "subSelectInQueryExpr"
    // EsperEPL2Ast.g:680:1: subSelectInQueryExpr : ^(i= IN_SUBSELECT_QUERY_EXPR subQueryExpr ) ;
    public final void subSelectInQueryExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:681:2: ( ^(i= IN_SUBSELECT_QUERY_EXPR subQueryExpr ) )
            // EsperEPL2Ast.g:681:4: ^(i= IN_SUBSELECT_QUERY_EXPR subQueryExpr )
            {
            pushStmtContext();
            i=(CommonTree)match(input,IN_SUBSELECT_QUERY_EXPR,FOLLOW_IN_SUBSELECT_QUERY_EXPR_in_subSelectInQueryExpr4362); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_subQueryExpr_in_subSelectInQueryExpr4364);
            subQueryExpr();

            state._fsp--;


            match(input, Token.UP, null); 
            leaveNode(i);

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "subSelectInQueryExpr"


    // $ANTLR start "subQueryExpr"
    // EsperEPL2Ast.g:684:1: subQueryExpr : ( DISTINCT )? selectionList subSelectFilterExpr ( whereClause[true] )? ;
    public final void subQueryExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:685:2: ( ( DISTINCT )? selectionList subSelectFilterExpr ( whereClause[true] )? )
            // EsperEPL2Ast.g:685:4: ( DISTINCT )? selectionList subSelectFilterExpr ( whereClause[true] )?
            {
            // EsperEPL2Ast.g:685:4: ( DISTINCT )?
            int alt226=2;
            int LA226_0 = input.LA(1);

            if ( (LA226_0==DISTINCT) ) {
                alt226=1;
            }
            switch (alt226) {
                case 1 :
                    // EsperEPL2Ast.g:685:4: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_subQueryExpr4380); 

                    }
                    break;

            }

            pushFollow(FOLLOW_selectionList_in_subQueryExpr4383);
            selectionList();

            state._fsp--;

            pushFollow(FOLLOW_subSelectFilterExpr_in_subQueryExpr4385);
            subSelectFilterExpr();

            state._fsp--;

            // EsperEPL2Ast.g:685:48: ( whereClause[true] )?
            int alt227=2;
            int LA227_0 = input.LA(1);

            if ( (LA227_0==WHERE_EXPR) ) {
                alt227=1;
            }
            switch (alt227) {
                case 1 :
                    // EsperEPL2Ast.g:685:49: whereClause[true]
                    {
                    pushFollow(FOLLOW_whereClause_in_subQueryExpr4388);
                    whereClause(true);

                    state._fsp--;


                    }
                    break;

            }


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "subQueryExpr"


    // $ANTLR start "subSelectFilterExpr"
    // EsperEPL2Ast.g:688:1: subSelectFilterExpr : ^(v= STREAM_EXPR eventFilterExpr[true] ( viewListExpr )? ( IDENT )? ( RETAINUNION )? ( RETAININTERSECTION )? ) ;
    public final void subSelectFilterExpr() throws RecognitionException {
        CommonTree v=null;

        try {
            // EsperEPL2Ast.g:689:2: ( ^(v= STREAM_EXPR eventFilterExpr[true] ( viewListExpr )? ( IDENT )? ( RETAINUNION )? ( RETAININTERSECTION )? ) )
            // EsperEPL2Ast.g:689:4: ^(v= STREAM_EXPR eventFilterExpr[true] ( viewListExpr )? ( IDENT )? ( RETAINUNION )? ( RETAININTERSECTION )? )
            {
            v=(CommonTree)match(input,STREAM_EXPR,FOLLOW_STREAM_EXPR_in_subSelectFilterExpr4406); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_eventFilterExpr_in_subSelectFilterExpr4408);
            eventFilterExpr(true);

            state._fsp--;

            // EsperEPL2Ast.g:689:42: ( viewListExpr )?
            int alt228=2;
            int LA228_0 = input.LA(1);

            if ( (LA228_0==VIEW_EXPR) ) {
                alt228=1;
            }
            switch (alt228) {
                case 1 :
                    // EsperEPL2Ast.g:689:43: viewListExpr
                    {
                    pushFollow(FOLLOW_viewListExpr_in_subSelectFilterExpr4412);
                    viewListExpr();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:689:58: ( IDENT )?
            int alt229=2;
            int LA229_0 = input.LA(1);

            if ( (LA229_0==IDENT) ) {
                alt229=1;
            }
            switch (alt229) {
                case 1 :
                    // EsperEPL2Ast.g:689:59: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_subSelectFilterExpr4417); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:689:67: ( RETAINUNION )?
            int alt230=2;
            int LA230_0 = input.LA(1);

            if ( (LA230_0==RETAINUNION) ) {
                alt230=1;
            }
            switch (alt230) {
                case 1 :
                    // EsperEPL2Ast.g:689:67: RETAINUNION
                    {
                    match(input,RETAINUNION,FOLLOW_RETAINUNION_in_subSelectFilterExpr4421); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:689:80: ( RETAININTERSECTION )?
            int alt231=2;
            int LA231_0 = input.LA(1);

            if ( (LA231_0==RETAININTERSECTION) ) {
                alt231=1;
            }
            switch (alt231) {
                case 1 :
                    // EsperEPL2Ast.g:689:80: RETAININTERSECTION
                    {
                    match(input,RETAININTERSECTION,FOLLOW_RETAININTERSECTION_in_subSelectFilterExpr4424); 

                    }
                    break;

            }

             leaveNode(v); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "subSelectFilterExpr"


    // $ANTLR start "caseExpr"
    // EsperEPL2Ast.g:692:1: caseExpr : ( ^(c= CASE ( valueExpr )* ) | ^(c= CASE2 ( valueExpr )* ) );
    public final void caseExpr() throws RecognitionException {
        CommonTree c=null;

        try {
            // EsperEPL2Ast.g:693:2: ( ^(c= CASE ( valueExpr )* ) | ^(c= CASE2 ( valueExpr )* ) )
            int alt234=2;
            int LA234_0 = input.LA(1);

            if ( (LA234_0==CASE) ) {
                alt234=1;
            }
            else if ( (LA234_0==CASE2) ) {
                alt234=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 234, 0, input);

                throw nvae;
            }
            switch (alt234) {
                case 1 :
                    // EsperEPL2Ast.g:693:4: ^(c= CASE ( valueExpr )* )
                    {
                    c=(CommonTree)match(input,CASE,FOLLOW_CASE_in_caseExpr4444); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:693:13: ( valueExpr )*
                        loop232:
                        do {
                            int alt232=2;
                            int LA232_0 = input.LA(1);

                            if ( ((LA232_0>=IN_SET && LA232_0<=REGEXP)||LA232_0==NOT_EXPR||(LA232_0>=SUM && LA232_0<=AVG)||(LA232_0>=COALESCE && LA232_0<=COUNT)||(LA232_0>=CASE && LA232_0<=CASE2)||LA232_0==ISTREAM||(LA232_0>=PREVIOUS && LA232_0<=EXISTS)||(LA232_0>=INSTANCEOF && LA232_0<=CURRENT_TIMESTAMP)||LA232_0==NEWKW||(LA232_0>=EVAL_AND_EXPR && LA232_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA232_0==EVENT_PROP_EXPR||LA232_0==CONCAT||(LA232_0>=LIB_FUNC_CHAIN && LA232_0<=DOT_EXPR)||LA232_0==ARRAY_EXPR||(LA232_0>=NOT_IN_SET && LA232_0<=NOT_REGEXP)||(LA232_0>=IN_RANGE && LA232_0<=SUBSELECT_EXPR)||(LA232_0>=EXISTS_SUBSELECT_EXPR && LA232_0<=NOT_IN_SUBSELECT_EXPR)||LA232_0==SUBSTITUTION||(LA232_0>=FIRST_AGGREG && LA232_0<=WINDOW_AGGREG)||(LA232_0>=INT_TYPE && LA232_0<=NULL_TYPE)||(LA232_0>=JSON_OBJECT && LA232_0<=JSON_ARRAY)||LA232_0==STAR||(LA232_0>=LT && LA232_0<=GT)||(LA232_0>=BOR && LA232_0<=PLUS)||(LA232_0>=BAND && LA232_0<=BXOR)||(LA232_0>=LE && LA232_0<=GE)||(LA232_0>=MINUS && LA232_0<=MOD)||(LA232_0>=EVAL_IS_GROUP_EXPR && LA232_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                                alt232=1;
                            }


                            switch (alt232) {
                        	case 1 :
                        	    // EsperEPL2Ast.g:693:14: valueExpr
                        	    {
                        	    pushFollow(FOLLOW_valueExpr_in_caseExpr4447);
                        	    valueExpr();

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop232;
                            }
                        } while (true);


                        match(input, Token.UP, null); 
                    }
                     leaveNode(c); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:694:4: ^(c= CASE2 ( valueExpr )* )
                    {
                    c=(CommonTree)match(input,CASE2,FOLLOW_CASE2_in_caseExpr4460); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:694:14: ( valueExpr )*
                        loop233:
                        do {
                            int alt233=2;
                            int LA233_0 = input.LA(1);

                            if ( ((LA233_0>=IN_SET && LA233_0<=REGEXP)||LA233_0==NOT_EXPR||(LA233_0>=SUM && LA233_0<=AVG)||(LA233_0>=COALESCE && LA233_0<=COUNT)||(LA233_0>=CASE && LA233_0<=CASE2)||LA233_0==ISTREAM||(LA233_0>=PREVIOUS && LA233_0<=EXISTS)||(LA233_0>=INSTANCEOF && LA233_0<=CURRENT_TIMESTAMP)||LA233_0==NEWKW||(LA233_0>=EVAL_AND_EXPR && LA233_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA233_0==EVENT_PROP_EXPR||LA233_0==CONCAT||(LA233_0>=LIB_FUNC_CHAIN && LA233_0<=DOT_EXPR)||LA233_0==ARRAY_EXPR||(LA233_0>=NOT_IN_SET && LA233_0<=NOT_REGEXP)||(LA233_0>=IN_RANGE && LA233_0<=SUBSELECT_EXPR)||(LA233_0>=EXISTS_SUBSELECT_EXPR && LA233_0<=NOT_IN_SUBSELECT_EXPR)||LA233_0==SUBSTITUTION||(LA233_0>=FIRST_AGGREG && LA233_0<=WINDOW_AGGREG)||(LA233_0>=INT_TYPE && LA233_0<=NULL_TYPE)||(LA233_0>=JSON_OBJECT && LA233_0<=JSON_ARRAY)||LA233_0==STAR||(LA233_0>=LT && LA233_0<=GT)||(LA233_0>=BOR && LA233_0<=PLUS)||(LA233_0>=BAND && LA233_0<=BXOR)||(LA233_0>=LE && LA233_0<=GE)||(LA233_0>=MINUS && LA233_0<=MOD)||(LA233_0>=EVAL_IS_GROUP_EXPR && LA233_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                                alt233=1;
                            }


                            switch (alt233) {
                        	case 1 :
                        	    // EsperEPL2Ast.g:694:15: valueExpr
                        	    {
                        	    pushFollow(FOLLOW_valueExpr_in_caseExpr4463);
                        	    valueExpr();

                        	    state._fsp--;


                        	    }
                        	    break;

                        	default :
                        	    break loop233;
                            }
                        } while (true);


                        match(input, Token.UP, null); 
                    }
                     leaveNode(c); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "caseExpr"


    // $ANTLR start "inExpr"
    // EsperEPL2Ast.g:697:1: inExpr : ( ^(i= IN_SET valueExpr ( LPAREN | LBRACK ) valueExpr ( valueExpr )* ( RPAREN | RBRACK ) ) | ^(i= NOT_IN_SET valueExpr ( LPAREN | LBRACK ) valueExpr ( valueExpr )* ( RPAREN | RBRACK ) ) | ^(i= IN_RANGE valueExpr ( LPAREN | LBRACK ) valueExpr valueExpr ( RPAREN | RBRACK ) ) | ^(i= NOT_IN_RANGE valueExpr ( LPAREN | LBRACK ) valueExpr valueExpr ( RPAREN | RBRACK ) ) );
    public final void inExpr() throws RecognitionException {
        CommonTree i=null;

        try {
            // EsperEPL2Ast.g:698:2: ( ^(i= IN_SET valueExpr ( LPAREN | LBRACK ) valueExpr ( valueExpr )* ( RPAREN | RBRACK ) ) | ^(i= NOT_IN_SET valueExpr ( LPAREN | LBRACK ) valueExpr ( valueExpr )* ( RPAREN | RBRACK ) ) | ^(i= IN_RANGE valueExpr ( LPAREN | LBRACK ) valueExpr valueExpr ( RPAREN | RBRACK ) ) | ^(i= NOT_IN_RANGE valueExpr ( LPAREN | LBRACK ) valueExpr valueExpr ( RPAREN | RBRACK ) ) )
            int alt237=4;
            switch ( input.LA(1) ) {
            case IN_SET:
                {
                alt237=1;
                }
                break;
            case NOT_IN_SET:
                {
                alt237=2;
                }
                break;
            case IN_RANGE:
                {
                alt237=3;
                }
                break;
            case NOT_IN_RANGE:
                {
                alt237=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 237, 0, input);

                throw nvae;
            }

            switch (alt237) {
                case 1 :
                    // EsperEPL2Ast.g:698:4: ^(i= IN_SET valueExpr ( LPAREN | LBRACK ) valueExpr ( valueExpr )* ( RPAREN | RBRACK ) )
                    {
                    i=(CommonTree)match(input,IN_SET,FOLLOW_IN_SET_in_inExpr4483); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_inExpr4485);
                    valueExpr();

                    state._fsp--;

                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_valueExpr_in_inExpr4493);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:698:51: ( valueExpr )*
                    loop235:
                    do {
                        int alt235=2;
                        int LA235_0 = input.LA(1);

                        if ( ((LA235_0>=IN_SET && LA235_0<=REGEXP)||LA235_0==NOT_EXPR||(LA235_0>=SUM && LA235_0<=AVG)||(LA235_0>=COALESCE && LA235_0<=COUNT)||(LA235_0>=CASE && LA235_0<=CASE2)||LA235_0==ISTREAM||(LA235_0>=PREVIOUS && LA235_0<=EXISTS)||(LA235_0>=INSTANCEOF && LA235_0<=CURRENT_TIMESTAMP)||LA235_0==NEWKW||(LA235_0>=EVAL_AND_EXPR && LA235_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA235_0==EVENT_PROP_EXPR||LA235_0==CONCAT||(LA235_0>=LIB_FUNC_CHAIN && LA235_0<=DOT_EXPR)||LA235_0==ARRAY_EXPR||(LA235_0>=NOT_IN_SET && LA235_0<=NOT_REGEXP)||(LA235_0>=IN_RANGE && LA235_0<=SUBSELECT_EXPR)||(LA235_0>=EXISTS_SUBSELECT_EXPR && LA235_0<=NOT_IN_SUBSELECT_EXPR)||LA235_0==SUBSTITUTION||(LA235_0>=FIRST_AGGREG && LA235_0<=WINDOW_AGGREG)||(LA235_0>=INT_TYPE && LA235_0<=NULL_TYPE)||(LA235_0>=JSON_OBJECT && LA235_0<=JSON_ARRAY)||LA235_0==STAR||(LA235_0>=LT && LA235_0<=GT)||(LA235_0>=BOR && LA235_0<=PLUS)||(LA235_0>=BAND && LA235_0<=BXOR)||(LA235_0>=LE && LA235_0<=GE)||(LA235_0>=MINUS && LA235_0<=MOD)||(LA235_0>=EVAL_IS_GROUP_EXPR && LA235_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt235=1;
                        }


                        switch (alt235) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:698:52: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_inExpr4496);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop235;
                        }
                    } while (true);

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 
                     leaveNode(i); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:699:4: ^(i= NOT_IN_SET valueExpr ( LPAREN | LBRACK ) valueExpr ( valueExpr )* ( RPAREN | RBRACK ) )
                    {
                    i=(CommonTree)match(input,NOT_IN_SET,FOLLOW_NOT_IN_SET_in_inExpr4515); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_inExpr4517);
                    valueExpr();

                    state._fsp--;

                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_valueExpr_in_inExpr4525);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:699:55: ( valueExpr )*
                    loop236:
                    do {
                        int alt236=2;
                        int LA236_0 = input.LA(1);

                        if ( ((LA236_0>=IN_SET && LA236_0<=REGEXP)||LA236_0==NOT_EXPR||(LA236_0>=SUM && LA236_0<=AVG)||(LA236_0>=COALESCE && LA236_0<=COUNT)||(LA236_0>=CASE && LA236_0<=CASE2)||LA236_0==ISTREAM||(LA236_0>=PREVIOUS && LA236_0<=EXISTS)||(LA236_0>=INSTANCEOF && LA236_0<=CURRENT_TIMESTAMP)||LA236_0==NEWKW||(LA236_0>=EVAL_AND_EXPR && LA236_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA236_0==EVENT_PROP_EXPR||LA236_0==CONCAT||(LA236_0>=LIB_FUNC_CHAIN && LA236_0<=DOT_EXPR)||LA236_0==ARRAY_EXPR||(LA236_0>=NOT_IN_SET && LA236_0<=NOT_REGEXP)||(LA236_0>=IN_RANGE && LA236_0<=SUBSELECT_EXPR)||(LA236_0>=EXISTS_SUBSELECT_EXPR && LA236_0<=NOT_IN_SUBSELECT_EXPR)||LA236_0==SUBSTITUTION||(LA236_0>=FIRST_AGGREG && LA236_0<=WINDOW_AGGREG)||(LA236_0>=INT_TYPE && LA236_0<=NULL_TYPE)||(LA236_0>=JSON_OBJECT && LA236_0<=JSON_ARRAY)||LA236_0==STAR||(LA236_0>=LT && LA236_0<=GT)||(LA236_0>=BOR && LA236_0<=PLUS)||(LA236_0>=BAND && LA236_0<=BXOR)||(LA236_0>=LE && LA236_0<=GE)||(LA236_0>=MINUS && LA236_0<=MOD)||(LA236_0>=EVAL_IS_GROUP_EXPR && LA236_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt236=1;
                        }


                        switch (alt236) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:699:56: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_inExpr4528);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop236;
                        }
                    } while (true);

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 
                     leaveNode(i); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:700:4: ^(i= IN_RANGE valueExpr ( LPAREN | LBRACK ) valueExpr valueExpr ( RPAREN | RBRACK ) )
                    {
                    i=(CommonTree)match(input,IN_RANGE,FOLLOW_IN_RANGE_in_inExpr4547); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_inExpr4549);
                    valueExpr();

                    state._fsp--;

                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_valueExpr_in_inExpr4557);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_inExpr4559);
                    valueExpr();

                    state._fsp--;

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 
                     leaveNode(i); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:701:4: ^(i= NOT_IN_RANGE valueExpr ( LPAREN | LBRACK ) valueExpr valueExpr ( RPAREN | RBRACK ) )
                    {
                    i=(CommonTree)match(input,NOT_IN_RANGE,FOLLOW_NOT_IN_RANGE_in_inExpr4576); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_inExpr4578);
                    valueExpr();

                    state._fsp--;

                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_valueExpr_in_inExpr4586);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_inExpr4588);
                    valueExpr();

                    state._fsp--;

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 
                     leaveNode(i); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "inExpr"


    // $ANTLR start "betweenExpr"
    // EsperEPL2Ast.g:704:1: betweenExpr : ( ^(b= BETWEEN valueExpr valueExpr valueExpr ) | ^(b= NOT_BETWEEN valueExpr valueExpr ( valueExpr )* ) );
    public final void betweenExpr() throws RecognitionException {
        CommonTree b=null;

        try {
            // EsperEPL2Ast.g:705:2: ( ^(b= BETWEEN valueExpr valueExpr valueExpr ) | ^(b= NOT_BETWEEN valueExpr valueExpr ( valueExpr )* ) )
            int alt239=2;
            int LA239_0 = input.LA(1);

            if ( (LA239_0==BETWEEN) ) {
                alt239=1;
            }
            else if ( (LA239_0==NOT_BETWEEN) ) {
                alt239=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 239, 0, input);

                throw nvae;
            }
            switch (alt239) {
                case 1 :
                    // EsperEPL2Ast.g:705:4: ^(b= BETWEEN valueExpr valueExpr valueExpr )
                    {
                    b=(CommonTree)match(input,BETWEEN,FOLLOW_BETWEEN_in_betweenExpr4613); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_betweenExpr4615);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_betweenExpr4617);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_betweenExpr4619);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(b); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:706:4: ^(b= NOT_BETWEEN valueExpr valueExpr ( valueExpr )* )
                    {
                    b=(CommonTree)match(input,NOT_BETWEEN,FOLLOW_NOT_BETWEEN_in_betweenExpr4630); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_betweenExpr4632);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_betweenExpr4634);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:706:40: ( valueExpr )*
                    loop238:
                    do {
                        int alt238=2;
                        int LA238_0 = input.LA(1);

                        if ( ((LA238_0>=IN_SET && LA238_0<=REGEXP)||LA238_0==NOT_EXPR||(LA238_0>=SUM && LA238_0<=AVG)||(LA238_0>=COALESCE && LA238_0<=COUNT)||(LA238_0>=CASE && LA238_0<=CASE2)||LA238_0==ISTREAM||(LA238_0>=PREVIOUS && LA238_0<=EXISTS)||(LA238_0>=INSTANCEOF && LA238_0<=CURRENT_TIMESTAMP)||LA238_0==NEWKW||(LA238_0>=EVAL_AND_EXPR && LA238_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA238_0==EVENT_PROP_EXPR||LA238_0==CONCAT||(LA238_0>=LIB_FUNC_CHAIN && LA238_0<=DOT_EXPR)||LA238_0==ARRAY_EXPR||(LA238_0>=NOT_IN_SET && LA238_0<=NOT_REGEXP)||(LA238_0>=IN_RANGE && LA238_0<=SUBSELECT_EXPR)||(LA238_0>=EXISTS_SUBSELECT_EXPR && LA238_0<=NOT_IN_SUBSELECT_EXPR)||LA238_0==SUBSTITUTION||(LA238_0>=FIRST_AGGREG && LA238_0<=WINDOW_AGGREG)||(LA238_0>=INT_TYPE && LA238_0<=NULL_TYPE)||(LA238_0>=JSON_OBJECT && LA238_0<=JSON_ARRAY)||LA238_0==STAR||(LA238_0>=LT && LA238_0<=GT)||(LA238_0>=BOR && LA238_0<=PLUS)||(LA238_0>=BAND && LA238_0<=BXOR)||(LA238_0>=LE && LA238_0<=GE)||(LA238_0>=MINUS && LA238_0<=MOD)||(LA238_0>=EVAL_IS_GROUP_EXPR && LA238_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt238=1;
                        }


                        switch (alt238) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:706:41: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_betweenExpr4637);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop238;
                        }
                    } while (true);


                    match(input, Token.UP, null); 
                     leaveNode(b); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "betweenExpr"


    // $ANTLR start "likeExpr"
    // EsperEPL2Ast.g:709:1: likeExpr : ( ^(l= LIKE valueExpr valueExpr ( valueExpr )? ) | ^(l= NOT_LIKE valueExpr valueExpr ( valueExpr )? ) );
    public final void likeExpr() throws RecognitionException {
        CommonTree l=null;

        try {
            // EsperEPL2Ast.g:710:2: ( ^(l= LIKE valueExpr valueExpr ( valueExpr )? ) | ^(l= NOT_LIKE valueExpr valueExpr ( valueExpr )? ) )
            int alt242=2;
            int LA242_0 = input.LA(1);

            if ( (LA242_0==LIKE) ) {
                alt242=1;
            }
            else if ( (LA242_0==NOT_LIKE) ) {
                alt242=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 242, 0, input);

                throw nvae;
            }
            switch (alt242) {
                case 1 :
                    // EsperEPL2Ast.g:710:4: ^(l= LIKE valueExpr valueExpr ( valueExpr )? )
                    {
                    l=(CommonTree)match(input,LIKE,FOLLOW_LIKE_in_likeExpr4657); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_likeExpr4659);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_likeExpr4661);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:710:33: ( valueExpr )?
                    int alt240=2;
                    int LA240_0 = input.LA(1);

                    if ( ((LA240_0>=IN_SET && LA240_0<=REGEXP)||LA240_0==NOT_EXPR||(LA240_0>=SUM && LA240_0<=AVG)||(LA240_0>=COALESCE && LA240_0<=COUNT)||(LA240_0>=CASE && LA240_0<=CASE2)||LA240_0==ISTREAM||(LA240_0>=PREVIOUS && LA240_0<=EXISTS)||(LA240_0>=INSTANCEOF && LA240_0<=CURRENT_TIMESTAMP)||LA240_0==NEWKW||(LA240_0>=EVAL_AND_EXPR && LA240_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA240_0==EVENT_PROP_EXPR||LA240_0==CONCAT||(LA240_0>=LIB_FUNC_CHAIN && LA240_0<=DOT_EXPR)||LA240_0==ARRAY_EXPR||(LA240_0>=NOT_IN_SET && LA240_0<=NOT_REGEXP)||(LA240_0>=IN_RANGE && LA240_0<=SUBSELECT_EXPR)||(LA240_0>=EXISTS_SUBSELECT_EXPR && LA240_0<=NOT_IN_SUBSELECT_EXPR)||LA240_0==SUBSTITUTION||(LA240_0>=FIRST_AGGREG && LA240_0<=WINDOW_AGGREG)||(LA240_0>=INT_TYPE && LA240_0<=NULL_TYPE)||(LA240_0>=JSON_OBJECT && LA240_0<=JSON_ARRAY)||LA240_0==STAR||(LA240_0>=LT && LA240_0<=GT)||(LA240_0>=BOR && LA240_0<=PLUS)||(LA240_0>=BAND && LA240_0<=BXOR)||(LA240_0>=LE && LA240_0<=GE)||(LA240_0>=MINUS && LA240_0<=MOD)||(LA240_0>=EVAL_IS_GROUP_EXPR && LA240_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                        alt240=1;
                    }
                    switch (alt240) {
                        case 1 :
                            // EsperEPL2Ast.g:710:34: valueExpr
                            {
                            pushFollow(FOLLOW_valueExpr_in_likeExpr4664);
                            valueExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(l); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:711:4: ^(l= NOT_LIKE valueExpr valueExpr ( valueExpr )? )
                    {
                    l=(CommonTree)match(input,NOT_LIKE,FOLLOW_NOT_LIKE_in_likeExpr4677); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_likeExpr4679);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_likeExpr4681);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:711:37: ( valueExpr )?
                    int alt241=2;
                    int LA241_0 = input.LA(1);

                    if ( ((LA241_0>=IN_SET && LA241_0<=REGEXP)||LA241_0==NOT_EXPR||(LA241_0>=SUM && LA241_0<=AVG)||(LA241_0>=COALESCE && LA241_0<=COUNT)||(LA241_0>=CASE && LA241_0<=CASE2)||LA241_0==ISTREAM||(LA241_0>=PREVIOUS && LA241_0<=EXISTS)||(LA241_0>=INSTANCEOF && LA241_0<=CURRENT_TIMESTAMP)||LA241_0==NEWKW||(LA241_0>=EVAL_AND_EXPR && LA241_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA241_0==EVENT_PROP_EXPR||LA241_0==CONCAT||(LA241_0>=LIB_FUNC_CHAIN && LA241_0<=DOT_EXPR)||LA241_0==ARRAY_EXPR||(LA241_0>=NOT_IN_SET && LA241_0<=NOT_REGEXP)||(LA241_0>=IN_RANGE && LA241_0<=SUBSELECT_EXPR)||(LA241_0>=EXISTS_SUBSELECT_EXPR && LA241_0<=NOT_IN_SUBSELECT_EXPR)||LA241_0==SUBSTITUTION||(LA241_0>=FIRST_AGGREG && LA241_0<=WINDOW_AGGREG)||(LA241_0>=INT_TYPE && LA241_0<=NULL_TYPE)||(LA241_0>=JSON_OBJECT && LA241_0<=JSON_ARRAY)||LA241_0==STAR||(LA241_0>=LT && LA241_0<=GT)||(LA241_0>=BOR && LA241_0<=PLUS)||(LA241_0>=BAND && LA241_0<=BXOR)||(LA241_0>=LE && LA241_0<=GE)||(LA241_0>=MINUS && LA241_0<=MOD)||(LA241_0>=EVAL_IS_GROUP_EXPR && LA241_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                        alt241=1;
                    }
                    switch (alt241) {
                        case 1 :
                            // EsperEPL2Ast.g:711:38: valueExpr
                            {
                            pushFollow(FOLLOW_valueExpr_in_likeExpr4684);
                            valueExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(l); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "likeExpr"


    // $ANTLR start "regExpExpr"
    // EsperEPL2Ast.g:714:1: regExpExpr : ( ^(r= REGEXP valueExpr valueExpr ) | ^(r= NOT_REGEXP valueExpr valueExpr ) );
    public final void regExpExpr() throws RecognitionException {
        CommonTree r=null;

        try {
            // EsperEPL2Ast.g:715:2: ( ^(r= REGEXP valueExpr valueExpr ) | ^(r= NOT_REGEXP valueExpr valueExpr ) )
            int alt243=2;
            int LA243_0 = input.LA(1);

            if ( (LA243_0==REGEXP) ) {
                alt243=1;
            }
            else if ( (LA243_0==NOT_REGEXP) ) {
                alt243=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 243, 0, input);

                throw nvae;
            }
            switch (alt243) {
                case 1 :
                    // EsperEPL2Ast.g:715:4: ^(r= REGEXP valueExpr valueExpr )
                    {
                    r=(CommonTree)match(input,REGEXP,FOLLOW_REGEXP_in_regExpExpr4703); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_regExpExpr4705);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_regExpExpr4707);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(r); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:716:4: ^(r= NOT_REGEXP valueExpr valueExpr )
                    {
                    r=(CommonTree)match(input,NOT_REGEXP,FOLLOW_NOT_REGEXP_in_regExpExpr4718); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_regExpExpr4720);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_regExpExpr4722);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(r); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "regExpExpr"


    // $ANTLR start "builtinFunc"
    // EsperEPL2Ast.g:719:1: builtinFunc : ( ^(f= SUM ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= AVG ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= COUNT ( ( DISTINCT )? valueExpr )? ( aggregationFilterExpr )? ) | ^(f= MEDIAN ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= STDDEV ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= AVEDEV ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= LAST_AGGREG ( DISTINCT )? ( accessValueExpr )? ( valueExpr )? ) | ^(f= FIRST_AGGREG ( DISTINCT )? ( accessValueExpr )? ( valueExpr )? ) | ^(f= WINDOW_AGGREG ( DISTINCT )? ( accessValueExpr )? ) | ^(f= COALESCE valueExpr valueExpr ( valueExpr )* ) | ^(f= PREVIOUS valueExpr ( valueExpr )? ) | ^(f= PREVIOUSTAIL valueExpr ( valueExpr )? ) | ^(f= PREVIOUSCOUNT valueExpr ) | ^(f= PREVIOUSWINDOW valueExpr ) | ^(f= PRIOR c= NUM_INT eventPropertyExpr[true] ) | ^(f= INSTANCEOF valueExpr CLASS_IDENT ( CLASS_IDENT )* ) | ^(f= TYPEOF valueExpr ) | ^(f= CAST valueExpr CLASS_IDENT ) | ^(f= EXISTS eventPropertyExpr[true] ) | ^(f= CURRENT_TIMESTAMP ) | ^(f= ISTREAM ) );
    public final void builtinFunc() throws RecognitionException {
        CommonTree f=null;
        CommonTree c=null;

        try {
            // EsperEPL2Ast.g:720:2: ( ^(f= SUM ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= AVG ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= COUNT ( ( DISTINCT )? valueExpr )? ( aggregationFilterExpr )? ) | ^(f= MEDIAN ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= STDDEV ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= AVEDEV ( DISTINCT )? valueExpr ( aggregationFilterExpr )? ) | ^(f= LAST_AGGREG ( DISTINCT )? ( accessValueExpr )? ( valueExpr )? ) | ^(f= FIRST_AGGREG ( DISTINCT )? ( accessValueExpr )? ( valueExpr )? ) | ^(f= WINDOW_AGGREG ( DISTINCT )? ( accessValueExpr )? ) | ^(f= COALESCE valueExpr valueExpr ( valueExpr )* ) | ^(f= PREVIOUS valueExpr ( valueExpr )? ) | ^(f= PREVIOUSTAIL valueExpr ( valueExpr )? ) | ^(f= PREVIOUSCOUNT valueExpr ) | ^(f= PREVIOUSWINDOW valueExpr ) | ^(f= PRIOR c= NUM_INT eventPropertyExpr[true] ) | ^(f= INSTANCEOF valueExpr CLASS_IDENT ( CLASS_IDENT )* ) | ^(f= TYPEOF valueExpr ) | ^(f= CAST valueExpr CLASS_IDENT ) | ^(f= EXISTS eventPropertyExpr[true] ) | ^(f= CURRENT_TIMESTAMP ) | ^(f= ISTREAM ) )
            int alt269=21;
            switch ( input.LA(1) ) {
            case SUM:
                {
                alt269=1;
                }
                break;
            case AVG:
                {
                alt269=2;
                }
                break;
            case COUNT:
                {
                alt269=3;
                }
                break;
            case MEDIAN:
                {
                alt269=4;
                }
                break;
            case STDDEV:
                {
                alt269=5;
                }
                break;
            case AVEDEV:
                {
                alt269=6;
                }
                break;
            case LAST_AGGREG:
                {
                alt269=7;
                }
                break;
            case FIRST_AGGREG:
                {
                alt269=8;
                }
                break;
            case WINDOW_AGGREG:
                {
                alt269=9;
                }
                break;
            case COALESCE:
                {
                alt269=10;
                }
                break;
            case PREVIOUS:
                {
                alt269=11;
                }
                break;
            case PREVIOUSTAIL:
                {
                alt269=12;
                }
                break;
            case PREVIOUSCOUNT:
                {
                alt269=13;
                }
                break;
            case PREVIOUSWINDOW:
                {
                alt269=14;
                }
                break;
            case PRIOR:
                {
                alt269=15;
                }
                break;
            case INSTANCEOF:
                {
                alt269=16;
                }
                break;
            case TYPEOF:
                {
                alt269=17;
                }
                break;
            case CAST:
                {
                alt269=18;
                }
                break;
            case EXISTS:
                {
                alt269=19;
                }
                break;
            case CURRENT_TIMESTAMP:
                {
                alt269=20;
                }
                break;
            case ISTREAM:
                {
                alt269=21;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 269, 0, input);

                throw nvae;
            }

            switch (alt269) {
                case 1 :
                    // EsperEPL2Ast.g:720:5: ^(f= SUM ( DISTINCT )? valueExpr ( aggregationFilterExpr )? )
                    {
                    f=(CommonTree)match(input,SUM,FOLLOW_SUM_in_builtinFunc4741); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:720:13: ( DISTINCT )?
                    int alt244=2;
                    int LA244_0 = input.LA(1);

                    if ( (LA244_0==DISTINCT) ) {
                        alt244=1;
                    }
                    switch (alt244) {
                        case 1 :
                            // EsperEPL2Ast.g:720:14: DISTINCT
                            {
                            match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4744); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4748);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:720:35: ( aggregationFilterExpr )?
                    int alt245=2;
                    int LA245_0 = input.LA(1);

                    if ( (LA245_0==AGG_FILTER_EXPR) ) {
                        alt245=1;
                    }
                    switch (alt245) {
                        case 1 :
                            // EsperEPL2Ast.g:720:35: aggregationFilterExpr
                            {
                            pushFollow(FOLLOW_aggregationFilterExpr_in_builtinFunc4750);
                            aggregationFilterExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:721:4: ^(f= AVG ( DISTINCT )? valueExpr ( aggregationFilterExpr )? )
                    {
                    f=(CommonTree)match(input,AVG,FOLLOW_AVG_in_builtinFunc4762); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:721:12: ( DISTINCT )?
                    int alt246=2;
                    int LA246_0 = input.LA(1);

                    if ( (LA246_0==DISTINCT) ) {
                        alt246=1;
                    }
                    switch (alt246) {
                        case 1 :
                            // EsperEPL2Ast.g:721:13: DISTINCT
                            {
                            match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4765); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4769);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:721:34: ( aggregationFilterExpr )?
                    int alt247=2;
                    int LA247_0 = input.LA(1);

                    if ( (LA247_0==AGG_FILTER_EXPR) ) {
                        alt247=1;
                    }
                    switch (alt247) {
                        case 1 :
                            // EsperEPL2Ast.g:721:34: aggregationFilterExpr
                            {
                            pushFollow(FOLLOW_aggregationFilterExpr_in_builtinFunc4771);
                            aggregationFilterExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:722:4: ^(f= COUNT ( ( DISTINCT )? valueExpr )? ( aggregationFilterExpr )? )
                    {
                    f=(CommonTree)match(input,COUNT,FOLLOW_COUNT_in_builtinFunc4783); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:722:14: ( ( DISTINCT )? valueExpr )?
                        int alt249=2;
                        int LA249_0 = input.LA(1);

                        if ( ((LA249_0>=IN_SET && LA249_0<=REGEXP)||LA249_0==NOT_EXPR||(LA249_0>=SUM && LA249_0<=AVG)||(LA249_0>=COALESCE && LA249_0<=COUNT)||(LA249_0>=CASE && LA249_0<=CASE2)||LA249_0==DISTINCT||LA249_0==ISTREAM||(LA249_0>=PREVIOUS && LA249_0<=EXISTS)||(LA249_0>=INSTANCEOF && LA249_0<=CURRENT_TIMESTAMP)||LA249_0==NEWKW||(LA249_0>=EVAL_AND_EXPR && LA249_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA249_0==EVENT_PROP_EXPR||LA249_0==CONCAT||(LA249_0>=LIB_FUNC_CHAIN && LA249_0<=DOT_EXPR)||LA249_0==ARRAY_EXPR||(LA249_0>=NOT_IN_SET && LA249_0<=NOT_REGEXP)||(LA249_0>=IN_RANGE && LA249_0<=SUBSELECT_EXPR)||(LA249_0>=EXISTS_SUBSELECT_EXPR && LA249_0<=NOT_IN_SUBSELECT_EXPR)||LA249_0==SUBSTITUTION||(LA249_0>=FIRST_AGGREG && LA249_0<=WINDOW_AGGREG)||(LA249_0>=INT_TYPE && LA249_0<=NULL_TYPE)||(LA249_0>=JSON_OBJECT && LA249_0<=JSON_ARRAY)||LA249_0==STAR||(LA249_0>=LT && LA249_0<=GT)||(LA249_0>=BOR && LA249_0<=PLUS)||(LA249_0>=BAND && LA249_0<=BXOR)||(LA249_0>=LE && LA249_0<=GE)||(LA249_0>=MINUS && LA249_0<=MOD)||(LA249_0>=EVAL_IS_GROUP_EXPR && LA249_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt249=1;
                        }
                        switch (alt249) {
                            case 1 :
                                // EsperEPL2Ast.g:722:15: ( DISTINCT )? valueExpr
                                {
                                // EsperEPL2Ast.g:722:15: ( DISTINCT )?
                                int alt248=2;
                                int LA248_0 = input.LA(1);

                                if ( (LA248_0==DISTINCT) ) {
                                    alt248=1;
                                }
                                switch (alt248) {
                                    case 1 :
                                        // EsperEPL2Ast.g:722:16: DISTINCT
                                        {
                                        match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4787); 

                                        }
                                        break;

                                }

                                pushFollow(FOLLOW_valueExpr_in_builtinFunc4791);
                                valueExpr();

                                state._fsp--;


                                }
                                break;

                        }

                        // EsperEPL2Ast.g:722:39: ( aggregationFilterExpr )?
                        int alt250=2;
                        int LA250_0 = input.LA(1);

                        if ( (LA250_0==AGG_FILTER_EXPR) ) {
                            alt250=1;
                        }
                        switch (alt250) {
                            case 1 :
                                // EsperEPL2Ast.g:722:39: aggregationFilterExpr
                                {
                                pushFollow(FOLLOW_aggregationFilterExpr_in_builtinFunc4795);
                                aggregationFilterExpr();

                                state._fsp--;


                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }
                     leaveNode(f); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:723:4: ^(f= MEDIAN ( DISTINCT )? valueExpr ( aggregationFilterExpr )? )
                    {
                    f=(CommonTree)match(input,MEDIAN,FOLLOW_MEDIAN_in_builtinFunc4807); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:723:15: ( DISTINCT )?
                    int alt251=2;
                    int LA251_0 = input.LA(1);

                    if ( (LA251_0==DISTINCT) ) {
                        alt251=1;
                    }
                    switch (alt251) {
                        case 1 :
                            // EsperEPL2Ast.g:723:16: DISTINCT
                            {
                            match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4810); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4814);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:723:37: ( aggregationFilterExpr )?
                    int alt252=2;
                    int LA252_0 = input.LA(1);

                    if ( (LA252_0==AGG_FILTER_EXPR) ) {
                        alt252=1;
                    }
                    switch (alt252) {
                        case 1 :
                            // EsperEPL2Ast.g:723:37: aggregationFilterExpr
                            {
                            pushFollow(FOLLOW_aggregationFilterExpr_in_builtinFunc4816);
                            aggregationFilterExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:724:4: ^(f= STDDEV ( DISTINCT )? valueExpr ( aggregationFilterExpr )? )
                    {
                    f=(CommonTree)match(input,STDDEV,FOLLOW_STDDEV_in_builtinFunc4828); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:724:15: ( DISTINCT )?
                    int alt253=2;
                    int LA253_0 = input.LA(1);

                    if ( (LA253_0==DISTINCT) ) {
                        alt253=1;
                    }
                    switch (alt253) {
                        case 1 :
                            // EsperEPL2Ast.g:724:16: DISTINCT
                            {
                            match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4831); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4835);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:724:37: ( aggregationFilterExpr )?
                    int alt254=2;
                    int LA254_0 = input.LA(1);

                    if ( (LA254_0==AGG_FILTER_EXPR) ) {
                        alt254=1;
                    }
                    switch (alt254) {
                        case 1 :
                            // EsperEPL2Ast.g:724:37: aggregationFilterExpr
                            {
                            pushFollow(FOLLOW_aggregationFilterExpr_in_builtinFunc4837);
                            aggregationFilterExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:725:4: ^(f= AVEDEV ( DISTINCT )? valueExpr ( aggregationFilterExpr )? )
                    {
                    f=(CommonTree)match(input,AVEDEV,FOLLOW_AVEDEV_in_builtinFunc4849); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:725:15: ( DISTINCT )?
                    int alt255=2;
                    int LA255_0 = input.LA(1);

                    if ( (LA255_0==DISTINCT) ) {
                        alt255=1;
                    }
                    switch (alt255) {
                        case 1 :
                            // EsperEPL2Ast.g:725:16: DISTINCT
                            {
                            match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4852); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4856);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:725:37: ( aggregationFilterExpr )?
                    int alt256=2;
                    int LA256_0 = input.LA(1);

                    if ( (LA256_0==AGG_FILTER_EXPR) ) {
                        alt256=1;
                    }
                    switch (alt256) {
                        case 1 :
                            // EsperEPL2Ast.g:725:37: aggregationFilterExpr
                            {
                            pushFollow(FOLLOW_aggregationFilterExpr_in_builtinFunc4858);
                            aggregationFilterExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:726:4: ^(f= LAST_AGGREG ( DISTINCT )? ( accessValueExpr )? ( valueExpr )? )
                    {
                    f=(CommonTree)match(input,LAST_AGGREG,FOLLOW_LAST_AGGREG_in_builtinFunc4870); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:726:20: ( DISTINCT )?
                        int alt257=2;
                        int LA257_0 = input.LA(1);

                        if ( (LA257_0==DISTINCT) ) {
                            alt257=1;
                        }
                        switch (alt257) {
                            case 1 :
                                // EsperEPL2Ast.g:726:21: DISTINCT
                                {
                                match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4873); 

                                }
                                break;

                        }

                        // EsperEPL2Ast.g:726:32: ( accessValueExpr )?
                        int alt258=2;
                        int LA258_0 = input.LA(1);

                        if ( (LA258_0==ACCESS_AGG) ) {
                            alt258=1;
                        }
                        switch (alt258) {
                            case 1 :
                                // EsperEPL2Ast.g:726:32: accessValueExpr
                                {
                                pushFollow(FOLLOW_accessValueExpr_in_builtinFunc4877);
                                accessValueExpr();

                                state._fsp--;


                                }
                                break;

                        }

                        // EsperEPL2Ast.g:726:49: ( valueExpr )?
                        int alt259=2;
                        int LA259_0 = input.LA(1);

                        if ( ((LA259_0>=IN_SET && LA259_0<=REGEXP)||LA259_0==NOT_EXPR||(LA259_0>=SUM && LA259_0<=AVG)||(LA259_0>=COALESCE && LA259_0<=COUNT)||(LA259_0>=CASE && LA259_0<=CASE2)||LA259_0==ISTREAM||(LA259_0>=PREVIOUS && LA259_0<=EXISTS)||(LA259_0>=INSTANCEOF && LA259_0<=CURRENT_TIMESTAMP)||LA259_0==NEWKW||(LA259_0>=EVAL_AND_EXPR && LA259_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA259_0==EVENT_PROP_EXPR||LA259_0==CONCAT||(LA259_0>=LIB_FUNC_CHAIN && LA259_0<=DOT_EXPR)||LA259_0==ARRAY_EXPR||(LA259_0>=NOT_IN_SET && LA259_0<=NOT_REGEXP)||(LA259_0>=IN_RANGE && LA259_0<=SUBSELECT_EXPR)||(LA259_0>=EXISTS_SUBSELECT_EXPR && LA259_0<=NOT_IN_SUBSELECT_EXPR)||LA259_0==SUBSTITUTION||(LA259_0>=FIRST_AGGREG && LA259_0<=WINDOW_AGGREG)||(LA259_0>=INT_TYPE && LA259_0<=NULL_TYPE)||(LA259_0>=JSON_OBJECT && LA259_0<=JSON_ARRAY)||LA259_0==STAR||(LA259_0>=LT && LA259_0<=GT)||(LA259_0>=BOR && LA259_0<=PLUS)||(LA259_0>=BAND && LA259_0<=BXOR)||(LA259_0>=LE && LA259_0<=GE)||(LA259_0>=MINUS && LA259_0<=MOD)||(LA259_0>=EVAL_IS_GROUP_EXPR && LA259_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt259=1;
                        }
                        switch (alt259) {
                            case 1 :
                                // EsperEPL2Ast.g:726:49: valueExpr
                                {
                                pushFollow(FOLLOW_valueExpr_in_builtinFunc4880);
                                valueExpr();

                                state._fsp--;


                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }
                     leaveNode(f); 

                    }
                    break;
                case 8 :
                    // EsperEPL2Ast.g:727:4: ^(f= FIRST_AGGREG ( DISTINCT )? ( accessValueExpr )? ( valueExpr )? )
                    {
                    f=(CommonTree)match(input,FIRST_AGGREG,FOLLOW_FIRST_AGGREG_in_builtinFunc4892); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:727:21: ( DISTINCT )?
                        int alt260=2;
                        int LA260_0 = input.LA(1);

                        if ( (LA260_0==DISTINCT) ) {
                            alt260=1;
                        }
                        switch (alt260) {
                            case 1 :
                                // EsperEPL2Ast.g:727:22: DISTINCT
                                {
                                match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4895); 

                                }
                                break;

                        }

                        // EsperEPL2Ast.g:727:33: ( accessValueExpr )?
                        int alt261=2;
                        int LA261_0 = input.LA(1);

                        if ( (LA261_0==ACCESS_AGG) ) {
                            alt261=1;
                        }
                        switch (alt261) {
                            case 1 :
                                // EsperEPL2Ast.g:727:33: accessValueExpr
                                {
                                pushFollow(FOLLOW_accessValueExpr_in_builtinFunc4899);
                                accessValueExpr();

                                state._fsp--;


                                }
                                break;

                        }

                        // EsperEPL2Ast.g:727:50: ( valueExpr )?
                        int alt262=2;
                        int LA262_0 = input.LA(1);

                        if ( ((LA262_0>=IN_SET && LA262_0<=REGEXP)||LA262_0==NOT_EXPR||(LA262_0>=SUM && LA262_0<=AVG)||(LA262_0>=COALESCE && LA262_0<=COUNT)||(LA262_0>=CASE && LA262_0<=CASE2)||LA262_0==ISTREAM||(LA262_0>=PREVIOUS && LA262_0<=EXISTS)||(LA262_0>=INSTANCEOF && LA262_0<=CURRENT_TIMESTAMP)||LA262_0==NEWKW||(LA262_0>=EVAL_AND_EXPR && LA262_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA262_0==EVENT_PROP_EXPR||LA262_0==CONCAT||(LA262_0>=LIB_FUNC_CHAIN && LA262_0<=DOT_EXPR)||LA262_0==ARRAY_EXPR||(LA262_0>=NOT_IN_SET && LA262_0<=NOT_REGEXP)||(LA262_0>=IN_RANGE && LA262_0<=SUBSELECT_EXPR)||(LA262_0>=EXISTS_SUBSELECT_EXPR && LA262_0<=NOT_IN_SUBSELECT_EXPR)||LA262_0==SUBSTITUTION||(LA262_0>=FIRST_AGGREG && LA262_0<=WINDOW_AGGREG)||(LA262_0>=INT_TYPE && LA262_0<=NULL_TYPE)||(LA262_0>=JSON_OBJECT && LA262_0<=JSON_ARRAY)||LA262_0==STAR||(LA262_0>=LT && LA262_0<=GT)||(LA262_0>=BOR && LA262_0<=PLUS)||(LA262_0>=BAND && LA262_0<=BXOR)||(LA262_0>=LE && LA262_0<=GE)||(LA262_0>=MINUS && LA262_0<=MOD)||(LA262_0>=EVAL_IS_GROUP_EXPR && LA262_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt262=1;
                        }
                        switch (alt262) {
                            case 1 :
                                // EsperEPL2Ast.g:727:50: valueExpr
                                {
                                pushFollow(FOLLOW_valueExpr_in_builtinFunc4902);
                                valueExpr();

                                state._fsp--;


                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }
                     leaveNode(f); 

                    }
                    break;
                case 9 :
                    // EsperEPL2Ast.g:728:4: ^(f= WINDOW_AGGREG ( DISTINCT )? ( accessValueExpr )? )
                    {
                    f=(CommonTree)match(input,WINDOW_AGGREG,FOLLOW_WINDOW_AGGREG_in_builtinFunc4914); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // EsperEPL2Ast.g:728:22: ( DISTINCT )?
                        int alt263=2;
                        int LA263_0 = input.LA(1);

                        if ( (LA263_0==DISTINCT) ) {
                            alt263=1;
                        }
                        switch (alt263) {
                            case 1 :
                                // EsperEPL2Ast.g:728:23: DISTINCT
                                {
                                match(input,DISTINCT,FOLLOW_DISTINCT_in_builtinFunc4917); 

                                }
                                break;

                        }

                        // EsperEPL2Ast.g:728:34: ( accessValueExpr )?
                        int alt264=2;
                        int LA264_0 = input.LA(1);

                        if ( (LA264_0==ACCESS_AGG) ) {
                            alt264=1;
                        }
                        switch (alt264) {
                            case 1 :
                                // EsperEPL2Ast.g:728:34: accessValueExpr
                                {
                                pushFollow(FOLLOW_accessValueExpr_in_builtinFunc4921);
                                accessValueExpr();

                                state._fsp--;


                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }
                     leaveNode(f); 

                    }
                    break;
                case 10 :
                    // EsperEPL2Ast.g:729:5: ^(f= COALESCE valueExpr valueExpr ( valueExpr )* )
                    {
                    f=(CommonTree)match(input,COALESCE,FOLLOW_COALESCE_in_builtinFunc4934); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4936);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4938);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:729:38: ( valueExpr )*
                    loop265:
                    do {
                        int alt265=2;
                        int LA265_0 = input.LA(1);

                        if ( ((LA265_0>=IN_SET && LA265_0<=REGEXP)||LA265_0==NOT_EXPR||(LA265_0>=SUM && LA265_0<=AVG)||(LA265_0>=COALESCE && LA265_0<=COUNT)||(LA265_0>=CASE && LA265_0<=CASE2)||LA265_0==ISTREAM||(LA265_0>=PREVIOUS && LA265_0<=EXISTS)||(LA265_0>=INSTANCEOF && LA265_0<=CURRENT_TIMESTAMP)||LA265_0==NEWKW||(LA265_0>=EVAL_AND_EXPR && LA265_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA265_0==EVENT_PROP_EXPR||LA265_0==CONCAT||(LA265_0>=LIB_FUNC_CHAIN && LA265_0<=DOT_EXPR)||LA265_0==ARRAY_EXPR||(LA265_0>=NOT_IN_SET && LA265_0<=NOT_REGEXP)||(LA265_0>=IN_RANGE && LA265_0<=SUBSELECT_EXPR)||(LA265_0>=EXISTS_SUBSELECT_EXPR && LA265_0<=NOT_IN_SUBSELECT_EXPR)||LA265_0==SUBSTITUTION||(LA265_0>=FIRST_AGGREG && LA265_0<=WINDOW_AGGREG)||(LA265_0>=INT_TYPE && LA265_0<=NULL_TYPE)||(LA265_0>=JSON_OBJECT && LA265_0<=JSON_ARRAY)||LA265_0==STAR||(LA265_0>=LT && LA265_0<=GT)||(LA265_0>=BOR && LA265_0<=PLUS)||(LA265_0>=BAND && LA265_0<=BXOR)||(LA265_0>=LE && LA265_0<=GE)||(LA265_0>=MINUS && LA265_0<=MOD)||(LA265_0>=EVAL_IS_GROUP_EXPR && LA265_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt265=1;
                        }


                        switch (alt265) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:729:39: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_builtinFunc4941);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop265;
                        }
                    } while (true);


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 11 :
                    // EsperEPL2Ast.g:730:5: ^(f= PREVIOUS valueExpr ( valueExpr )? )
                    {
                    f=(CommonTree)match(input,PREVIOUS,FOLLOW_PREVIOUS_in_builtinFunc4956); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4958);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:730:28: ( valueExpr )?
                    int alt266=2;
                    int LA266_0 = input.LA(1);

                    if ( ((LA266_0>=IN_SET && LA266_0<=REGEXP)||LA266_0==NOT_EXPR||(LA266_0>=SUM && LA266_0<=AVG)||(LA266_0>=COALESCE && LA266_0<=COUNT)||(LA266_0>=CASE && LA266_0<=CASE2)||LA266_0==ISTREAM||(LA266_0>=PREVIOUS && LA266_0<=EXISTS)||(LA266_0>=INSTANCEOF && LA266_0<=CURRENT_TIMESTAMP)||LA266_0==NEWKW||(LA266_0>=EVAL_AND_EXPR && LA266_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA266_0==EVENT_PROP_EXPR||LA266_0==CONCAT||(LA266_0>=LIB_FUNC_CHAIN && LA266_0<=DOT_EXPR)||LA266_0==ARRAY_EXPR||(LA266_0>=NOT_IN_SET && LA266_0<=NOT_REGEXP)||(LA266_0>=IN_RANGE && LA266_0<=SUBSELECT_EXPR)||(LA266_0>=EXISTS_SUBSELECT_EXPR && LA266_0<=NOT_IN_SUBSELECT_EXPR)||LA266_0==SUBSTITUTION||(LA266_0>=FIRST_AGGREG && LA266_0<=WINDOW_AGGREG)||(LA266_0>=INT_TYPE && LA266_0<=NULL_TYPE)||(LA266_0>=JSON_OBJECT && LA266_0<=JSON_ARRAY)||LA266_0==STAR||(LA266_0>=LT && LA266_0<=GT)||(LA266_0>=BOR && LA266_0<=PLUS)||(LA266_0>=BAND && LA266_0<=BXOR)||(LA266_0>=LE && LA266_0<=GE)||(LA266_0>=MINUS && LA266_0<=MOD)||(LA266_0>=EVAL_IS_GROUP_EXPR && LA266_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                        alt266=1;
                    }
                    switch (alt266) {
                        case 1 :
                            // EsperEPL2Ast.g:730:28: valueExpr
                            {
                            pushFollow(FOLLOW_valueExpr_in_builtinFunc4960);
                            valueExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 12 :
                    // EsperEPL2Ast.g:731:5: ^(f= PREVIOUSTAIL valueExpr ( valueExpr )? )
                    {
                    f=(CommonTree)match(input,PREVIOUSTAIL,FOLLOW_PREVIOUSTAIL_in_builtinFunc4973); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4975);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:731:32: ( valueExpr )?
                    int alt267=2;
                    int LA267_0 = input.LA(1);

                    if ( ((LA267_0>=IN_SET && LA267_0<=REGEXP)||LA267_0==NOT_EXPR||(LA267_0>=SUM && LA267_0<=AVG)||(LA267_0>=COALESCE && LA267_0<=COUNT)||(LA267_0>=CASE && LA267_0<=CASE2)||LA267_0==ISTREAM||(LA267_0>=PREVIOUS && LA267_0<=EXISTS)||(LA267_0>=INSTANCEOF && LA267_0<=CURRENT_TIMESTAMP)||LA267_0==NEWKW||(LA267_0>=EVAL_AND_EXPR && LA267_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA267_0==EVENT_PROP_EXPR||LA267_0==CONCAT||(LA267_0>=LIB_FUNC_CHAIN && LA267_0<=DOT_EXPR)||LA267_0==ARRAY_EXPR||(LA267_0>=NOT_IN_SET && LA267_0<=NOT_REGEXP)||(LA267_0>=IN_RANGE && LA267_0<=SUBSELECT_EXPR)||(LA267_0>=EXISTS_SUBSELECT_EXPR && LA267_0<=NOT_IN_SUBSELECT_EXPR)||LA267_0==SUBSTITUTION||(LA267_0>=FIRST_AGGREG && LA267_0<=WINDOW_AGGREG)||(LA267_0>=INT_TYPE && LA267_0<=NULL_TYPE)||(LA267_0>=JSON_OBJECT && LA267_0<=JSON_ARRAY)||LA267_0==STAR||(LA267_0>=LT && LA267_0<=GT)||(LA267_0>=BOR && LA267_0<=PLUS)||(LA267_0>=BAND && LA267_0<=BXOR)||(LA267_0>=LE && LA267_0<=GE)||(LA267_0>=MINUS && LA267_0<=MOD)||(LA267_0>=EVAL_IS_GROUP_EXPR && LA267_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                        alt267=1;
                    }
                    switch (alt267) {
                        case 1 :
                            // EsperEPL2Ast.g:731:32: valueExpr
                            {
                            pushFollow(FOLLOW_valueExpr_in_builtinFunc4977);
                            valueExpr();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 13 :
                    // EsperEPL2Ast.g:732:5: ^(f= PREVIOUSCOUNT valueExpr )
                    {
                    f=(CommonTree)match(input,PREVIOUSCOUNT,FOLLOW_PREVIOUSCOUNT_in_builtinFunc4990); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc4992);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 14 :
                    // EsperEPL2Ast.g:733:5: ^(f= PREVIOUSWINDOW valueExpr )
                    {
                    f=(CommonTree)match(input,PREVIOUSWINDOW,FOLLOW_PREVIOUSWINDOW_in_builtinFunc5004); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc5006);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 15 :
                    // EsperEPL2Ast.g:734:5: ^(f= PRIOR c= NUM_INT eventPropertyExpr[true] )
                    {
                    f=(CommonTree)match(input,PRIOR,FOLLOW_PRIOR_in_builtinFunc5018); 

                    match(input, Token.DOWN, null); 
                    c=(CommonTree)match(input,NUM_INT,FOLLOW_NUM_INT_in_builtinFunc5022); 
                    pushFollow(FOLLOW_eventPropertyExpr_in_builtinFunc5024);
                    eventPropertyExpr(true);

                    state._fsp--;


                    match(input, Token.UP, null); 
                    leaveNode(c); leaveNode(f);

                    }
                    break;
                case 16 :
                    // EsperEPL2Ast.g:735:5: ^(f= INSTANCEOF valueExpr CLASS_IDENT ( CLASS_IDENT )* )
                    {
                    f=(CommonTree)match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_builtinFunc5037); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc5039);
                    valueExpr();

                    state._fsp--;

                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_builtinFunc5041); 
                    // EsperEPL2Ast.g:735:42: ( CLASS_IDENT )*
                    loop268:
                    do {
                        int alt268=2;
                        int LA268_0 = input.LA(1);

                        if ( (LA268_0==CLASS_IDENT) ) {
                            alt268=1;
                        }


                        switch (alt268) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:735:43: CLASS_IDENT
                    	    {
                    	    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_builtinFunc5044); 

                    	    }
                    	    break;

                    	default :
                    	    break loop268;
                        }
                    } while (true);


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 17 :
                    // EsperEPL2Ast.g:736:5: ^(f= TYPEOF valueExpr )
                    {
                    f=(CommonTree)match(input,TYPEOF,FOLLOW_TYPEOF_in_builtinFunc5058); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc5060);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 18 :
                    // EsperEPL2Ast.g:737:5: ^(f= CAST valueExpr CLASS_IDENT )
                    {
                    f=(CommonTree)match(input,CAST,FOLLOW_CAST_in_builtinFunc5072); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_builtinFunc5074);
                    valueExpr();

                    state._fsp--;

                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_builtinFunc5076); 

                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 19 :
                    // EsperEPL2Ast.g:738:5: ^(f= EXISTS eventPropertyExpr[true] )
                    {
                    f=(CommonTree)match(input,EXISTS,FOLLOW_EXISTS_in_builtinFunc5088); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_eventPropertyExpr_in_builtinFunc5090);
                    eventPropertyExpr(true);

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(f); 

                    }
                    break;
                case 20 :
                    // EsperEPL2Ast.g:739:4: ^(f= CURRENT_TIMESTAMP )
                    {
                    f=(CommonTree)match(input,CURRENT_TIMESTAMP,FOLLOW_CURRENT_TIMESTAMP_in_builtinFunc5102); 



                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        match(input, Token.UP, null); 
                    }
                     leaveNode(f); 

                    }
                    break;
                case 21 :
                    // EsperEPL2Ast.g:740:4: ^(f= ISTREAM )
                    {
                    f=(CommonTree)match(input,ISTREAM,FOLLOW_ISTREAM_in_builtinFunc5115); 



                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        match(input, Token.UP, null); 
                    }
                     leaveNode(f); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "builtinFunc"


    // $ANTLR start "aggregationFilterExpr"
    // EsperEPL2Ast.g:743:1: aggregationFilterExpr : ^( AGG_FILTER_EXPR valueExpr ) ;
    public final void aggregationFilterExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:744:2: ( ^( AGG_FILTER_EXPR valueExpr ) )
            // EsperEPL2Ast.g:744:4: ^( AGG_FILTER_EXPR valueExpr )
            {
            match(input,AGG_FILTER_EXPR,FOLLOW_AGG_FILTER_EXPR_in_aggregationFilterExpr5132); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_aggregationFilterExpr5134);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "aggregationFilterExpr"


    // $ANTLR start "accessValueExpr"
    // EsperEPL2Ast.g:747:1: accessValueExpr : ^( ACCESS_AGG accessValueExprChoice ) ;
    public final void accessValueExpr() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:748:2: ( ^( ACCESS_AGG accessValueExprChoice ) )
            // EsperEPL2Ast.g:748:5: ^( ACCESS_AGG accessValueExprChoice )
            {
            match(input,ACCESS_AGG,FOLLOW_ACCESS_AGG_in_accessValueExpr5148); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_accessValueExprChoice_in_accessValueExpr5150);
            accessValueExprChoice();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "accessValueExpr"


    // $ANTLR start "accessValueExprChoice"
    // EsperEPL2Ast.g:751:1: accessValueExprChoice : ( PROPERTY_WILDCARD_SELECT | ^(s= PROPERTY_SELECTION_STREAM IDENT ( IDENT )? ) | valueExpr );
    public final void accessValueExprChoice() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:752:2: ( PROPERTY_WILDCARD_SELECT | ^(s= PROPERTY_SELECTION_STREAM IDENT ( IDENT )? ) | valueExpr )
            int alt271=3;
            switch ( input.LA(1) ) {
            case PROPERTY_WILDCARD_SELECT:
                {
                alt271=1;
                }
                break;
            case PROPERTY_SELECTION_STREAM:
                {
                alt271=2;
                }
                break;
            case IN_SET:
            case BETWEEN:
            case LIKE:
            case REGEXP:
            case NOT_EXPR:
            case SUM:
            case AVG:
            case COALESCE:
            case MEDIAN:
            case STDDEV:
            case AVEDEV:
            case COUNT:
            case CASE:
            case CASE2:
            case ISTREAM:
            case PREVIOUS:
            case PREVIOUSTAIL:
            case PREVIOUSCOUNT:
            case PREVIOUSWINDOW:
            case PRIOR:
            case EXISTS:
            case INSTANCEOF:
            case TYPEOF:
            case CAST:
            case CURRENT_TIMESTAMP:
            case NEWKW:
            case EVAL_AND_EXPR:
            case EVAL_OR_EXPR:
            case EVAL_EQUALS_EXPR:
            case EVAL_NOTEQUALS_EXPR:
            case EVAL_IS_EXPR:
            case EVAL_ISNOT_EXPR:
            case EVAL_EQUALS_GROUP_EXPR:
            case EVAL_NOTEQUALS_GROUP_EXPR:
            case EVENT_PROP_EXPR:
            case CONCAT:
            case LIB_FUNC_CHAIN:
            case DOT_EXPR:
            case ARRAY_EXPR:
            case NOT_IN_SET:
            case NOT_BETWEEN:
            case NOT_LIKE:
            case NOT_REGEXP:
            case IN_RANGE:
            case NOT_IN_RANGE:
            case SUBSELECT_EXPR:
            case EXISTS_SUBSELECT_EXPR:
            case IN_SUBSELECT_EXPR:
            case NOT_IN_SUBSELECT_EXPR:
            case SUBSTITUTION:
            case FIRST_AGGREG:
            case LAST_AGGREG:
            case WINDOW_AGGREG:
            case INT_TYPE:
            case LONG_TYPE:
            case FLOAT_TYPE:
            case DOUBLE_TYPE:
            case STRING_TYPE:
            case BOOL_TYPE:
            case NULL_TYPE:
            case JSON_OBJECT:
            case JSON_ARRAY:
            case STAR:
            case LT:
            case GT:
            case BOR:
            case PLUS:
            case BAND:
            case BXOR:
            case LE:
            case GE:
            case MINUS:
            case DIV:
            case MOD:
            case EVAL_IS_GROUP_EXPR:
            case EVAL_ISNOT_GROUP_EXPR:
                {
                alt271=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 271, 0, input);

                throw nvae;
            }

            switch (alt271) {
                case 1 :
                    // EsperEPL2Ast.g:752:4: PROPERTY_WILDCARD_SELECT
                    {
                    match(input,PROPERTY_WILDCARD_SELECT,FOLLOW_PROPERTY_WILDCARD_SELECT_in_accessValueExprChoice5165); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:752:31: ^(s= PROPERTY_SELECTION_STREAM IDENT ( IDENT )? )
                    {
                    s=(CommonTree)match(input,PROPERTY_SELECTION_STREAM,FOLLOW_PROPERTY_SELECTION_STREAM_in_accessValueExprChoice5172); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_accessValueExprChoice5174); 
                    // EsperEPL2Ast.g:752:67: ( IDENT )?
                    int alt270=2;
                    int LA270_0 = input.LA(1);

                    if ( (LA270_0==IDENT) ) {
                        alt270=1;
                    }
                    switch (alt270) {
                        case 1 :
                            // EsperEPL2Ast.g:752:67: IDENT
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_accessValueExprChoice5176); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:752:77: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_accessValueExprChoice5182);
                    valueExpr();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "accessValueExprChoice"


    // $ANTLR start "arrayExpr"
    // EsperEPL2Ast.g:755:1: arrayExpr : ^(a= ARRAY_EXPR ( valueExpr )* ) ;
    public final void arrayExpr() throws RecognitionException {
        CommonTree a=null;

        try {
            // EsperEPL2Ast.g:756:2: ( ^(a= ARRAY_EXPR ( valueExpr )* ) )
            // EsperEPL2Ast.g:756:4: ^(a= ARRAY_EXPR ( valueExpr )* )
            {
            a=(CommonTree)match(input,ARRAY_EXPR,FOLLOW_ARRAY_EXPR_in_arrayExpr5198); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:756:19: ( valueExpr )*
                loop272:
                do {
                    int alt272=2;
                    int LA272_0 = input.LA(1);

                    if ( ((LA272_0>=IN_SET && LA272_0<=REGEXP)||LA272_0==NOT_EXPR||(LA272_0>=SUM && LA272_0<=AVG)||(LA272_0>=COALESCE && LA272_0<=COUNT)||(LA272_0>=CASE && LA272_0<=CASE2)||LA272_0==ISTREAM||(LA272_0>=PREVIOUS && LA272_0<=EXISTS)||(LA272_0>=INSTANCEOF && LA272_0<=CURRENT_TIMESTAMP)||LA272_0==NEWKW||(LA272_0>=EVAL_AND_EXPR && LA272_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA272_0==EVENT_PROP_EXPR||LA272_0==CONCAT||(LA272_0>=LIB_FUNC_CHAIN && LA272_0<=DOT_EXPR)||LA272_0==ARRAY_EXPR||(LA272_0>=NOT_IN_SET && LA272_0<=NOT_REGEXP)||(LA272_0>=IN_RANGE && LA272_0<=SUBSELECT_EXPR)||(LA272_0>=EXISTS_SUBSELECT_EXPR && LA272_0<=NOT_IN_SUBSELECT_EXPR)||LA272_0==SUBSTITUTION||(LA272_0>=FIRST_AGGREG && LA272_0<=WINDOW_AGGREG)||(LA272_0>=INT_TYPE && LA272_0<=NULL_TYPE)||(LA272_0>=JSON_OBJECT && LA272_0<=JSON_ARRAY)||LA272_0==STAR||(LA272_0>=LT && LA272_0<=GT)||(LA272_0>=BOR && LA272_0<=PLUS)||(LA272_0>=BAND && LA272_0<=BXOR)||(LA272_0>=LE && LA272_0<=GE)||(LA272_0>=MINUS && LA272_0<=MOD)||(LA272_0>=EVAL_IS_GROUP_EXPR && LA272_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                        alt272=1;
                    }


                    switch (alt272) {
                	case 1 :
                	    // EsperEPL2Ast.g:756:20: valueExpr
                	    {
                	    pushFollow(FOLLOW_valueExpr_in_arrayExpr5201);
                	    valueExpr();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop272;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }
             leaveNode(a); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "arrayExpr"


    // $ANTLR start "arithmeticExpr"
    // EsperEPL2Ast.g:759:1: arithmeticExpr : ( ^(a= PLUS valueExpr valueExpr ) | ^(a= MINUS valueExpr valueExpr ) | ^(a= DIV valueExpr valueExpr ) | ^(a= STAR valueExpr valueExpr ) | ^(a= MOD valueExpr valueExpr ) | ^(a= BAND valueExpr valueExpr ) | ^(a= BOR valueExpr valueExpr ) | ^(a= BXOR valueExpr valueExpr ) | ^(a= CONCAT valueExpr valueExpr ( valueExpr )* ) );
    public final void arithmeticExpr() throws RecognitionException {
        CommonTree a=null;

        try {
            // EsperEPL2Ast.g:760:2: ( ^(a= PLUS valueExpr valueExpr ) | ^(a= MINUS valueExpr valueExpr ) | ^(a= DIV valueExpr valueExpr ) | ^(a= STAR valueExpr valueExpr ) | ^(a= MOD valueExpr valueExpr ) | ^(a= BAND valueExpr valueExpr ) | ^(a= BOR valueExpr valueExpr ) | ^(a= BXOR valueExpr valueExpr ) | ^(a= CONCAT valueExpr valueExpr ( valueExpr )* ) )
            int alt274=9;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                alt274=1;
                }
                break;
            case MINUS:
                {
                alt274=2;
                }
                break;
            case DIV:
                {
                alt274=3;
                }
                break;
            case STAR:
                {
                alt274=4;
                }
                break;
            case MOD:
                {
                alt274=5;
                }
                break;
            case BAND:
                {
                alt274=6;
                }
                break;
            case BOR:
                {
                alt274=7;
                }
                break;
            case BXOR:
                {
                alt274=8;
                }
                break;
            case CONCAT:
                {
                alt274=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 274, 0, input);

                throw nvae;
            }

            switch (alt274) {
                case 1 :
                    // EsperEPL2Ast.g:760:5: ^(a= PLUS valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_arithmeticExpr5222); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5224);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5226);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:761:5: ^(a= MINUS valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_arithmeticExpr5238); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5240);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5242);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:762:5: ^(a= DIV valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,DIV,FOLLOW_DIV_in_arithmeticExpr5254); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5256);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5258);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:763:4: ^(a= STAR valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,STAR,FOLLOW_STAR_in_arithmeticExpr5269); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5271);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5273);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:764:5: ^(a= MOD valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,MOD,FOLLOW_MOD_in_arithmeticExpr5285); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5287);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5289);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:765:4: ^(a= BAND valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,BAND,FOLLOW_BAND_in_arithmeticExpr5300); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5302);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5304);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:766:4: ^(a= BOR valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,BOR,FOLLOW_BOR_in_arithmeticExpr5315); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5317);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5319);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 8 :
                    // EsperEPL2Ast.g:767:4: ^(a= BXOR valueExpr valueExpr )
                    {
                    a=(CommonTree)match(input,BXOR,FOLLOW_BXOR_in_arithmeticExpr5330); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5332);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5334);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;
                case 9 :
                    // EsperEPL2Ast.g:768:5: ^(a= CONCAT valueExpr valueExpr ( valueExpr )* )
                    {
                    a=(CommonTree)match(input,CONCAT,FOLLOW_CONCAT_in_arithmeticExpr5346); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5348);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5350);
                    valueExpr();

                    state._fsp--;

                    // EsperEPL2Ast.g:768:36: ( valueExpr )*
                    loop273:
                    do {
                        int alt273=2;
                        int LA273_0 = input.LA(1);

                        if ( ((LA273_0>=IN_SET && LA273_0<=REGEXP)||LA273_0==NOT_EXPR||(LA273_0>=SUM && LA273_0<=AVG)||(LA273_0>=COALESCE && LA273_0<=COUNT)||(LA273_0>=CASE && LA273_0<=CASE2)||LA273_0==ISTREAM||(LA273_0>=PREVIOUS && LA273_0<=EXISTS)||(LA273_0>=INSTANCEOF && LA273_0<=CURRENT_TIMESTAMP)||LA273_0==NEWKW||(LA273_0>=EVAL_AND_EXPR && LA273_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA273_0==EVENT_PROP_EXPR||LA273_0==CONCAT||(LA273_0>=LIB_FUNC_CHAIN && LA273_0<=DOT_EXPR)||LA273_0==ARRAY_EXPR||(LA273_0>=NOT_IN_SET && LA273_0<=NOT_REGEXP)||(LA273_0>=IN_RANGE && LA273_0<=SUBSELECT_EXPR)||(LA273_0>=EXISTS_SUBSELECT_EXPR && LA273_0<=NOT_IN_SUBSELECT_EXPR)||LA273_0==SUBSTITUTION||(LA273_0>=FIRST_AGGREG && LA273_0<=WINDOW_AGGREG)||(LA273_0>=INT_TYPE && LA273_0<=NULL_TYPE)||(LA273_0>=JSON_OBJECT && LA273_0<=JSON_ARRAY)||LA273_0==STAR||(LA273_0>=LT && LA273_0<=GT)||(LA273_0>=BOR && LA273_0<=PLUS)||(LA273_0>=BAND && LA273_0<=BXOR)||(LA273_0>=LE && LA273_0<=GE)||(LA273_0>=MINUS && LA273_0<=MOD)||(LA273_0>=EVAL_IS_GROUP_EXPR && LA273_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt273=1;
                        }


                        switch (alt273) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:768:37: valueExpr
                    	    {
                    	    pushFollow(FOLLOW_valueExpr_in_arithmeticExpr5353);
                    	    valueExpr();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop273;
                        }
                    } while (true);


                    match(input, Token.UP, null); 
                     leaveNode(a); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "arithmeticExpr"


    // $ANTLR start "dotExpr"
    // EsperEPL2Ast.g:771:1: dotExpr : ^(d= DOT_EXPR valueExpr ( libFunctionWithClass )* ) ;
    public final void dotExpr() throws RecognitionException {
        CommonTree d=null;

        try {
            // EsperEPL2Ast.g:772:2: ( ^(d= DOT_EXPR valueExpr ( libFunctionWithClass )* ) )
            // EsperEPL2Ast.g:772:4: ^(d= DOT_EXPR valueExpr ( libFunctionWithClass )* )
            {
            d=(CommonTree)match(input,DOT_EXPR,FOLLOW_DOT_EXPR_in_dotExpr5373); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_dotExpr5375);
            valueExpr();

            state._fsp--;

            // EsperEPL2Ast.g:772:27: ( libFunctionWithClass )*
            loop275:
            do {
                int alt275=2;
                int LA275_0 = input.LA(1);

                if ( (LA275_0==LIB_FUNCTION) ) {
                    alt275=1;
                }


                switch (alt275) {
            	case 1 :
            	    // EsperEPL2Ast.g:772:27: libFunctionWithClass
            	    {
            	    pushFollow(FOLLOW_libFunctionWithClass_in_dotExpr5377);
            	    libFunctionWithClass();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop275;
                }
            } while (true);


            match(input, Token.UP, null); 
             leaveNode(d); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "dotExpr"


    // $ANTLR start "newExpr"
    // EsperEPL2Ast.g:775:1: newExpr : ^(n= NEWKW ( newAssign )* ) ;
    public final void newExpr() throws RecognitionException {
        CommonTree n=null;

        try {
            // EsperEPL2Ast.g:775:9: ( ^(n= NEWKW ( newAssign )* ) )
            // EsperEPL2Ast.g:775:11: ^(n= NEWKW ( newAssign )* )
            {
            n=(CommonTree)match(input,NEWKW,FOLLOW_NEWKW_in_newExpr5395); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // EsperEPL2Ast.g:775:21: ( newAssign )*
                loop276:
                do {
                    int alt276=2;
                    int LA276_0 = input.LA(1);

                    if ( (LA276_0==NEW_ITEM) ) {
                        alt276=1;
                    }


                    switch (alt276) {
                	case 1 :
                	    // EsperEPL2Ast.g:775:21: newAssign
                	    {
                	    pushFollow(FOLLOW_newAssign_in_newExpr5397);
                	    newAssign();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop276;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }
             leaveNode(n); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "newExpr"


    // $ANTLR start "newAssign"
    // EsperEPL2Ast.g:778:1: newAssign : ^( NEW_ITEM eventPropertyExpr[false] ( valueExpr )? ) ;
    public final void newAssign() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:779:2: ( ^( NEW_ITEM eventPropertyExpr[false] ( valueExpr )? ) )
            // EsperEPL2Ast.g:779:4: ^( NEW_ITEM eventPropertyExpr[false] ( valueExpr )? )
            {
            match(input,NEW_ITEM,FOLLOW_NEW_ITEM_in_newAssign5413); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_eventPropertyExpr_in_newAssign5415);
            eventPropertyExpr(false);

            state._fsp--;

            // EsperEPL2Ast.g:779:40: ( valueExpr )?
            int alt277=2;
            int LA277_0 = input.LA(1);

            if ( ((LA277_0>=IN_SET && LA277_0<=REGEXP)||LA277_0==NOT_EXPR||(LA277_0>=SUM && LA277_0<=AVG)||(LA277_0>=COALESCE && LA277_0<=COUNT)||(LA277_0>=CASE && LA277_0<=CASE2)||LA277_0==ISTREAM||(LA277_0>=PREVIOUS && LA277_0<=EXISTS)||(LA277_0>=INSTANCEOF && LA277_0<=CURRENT_TIMESTAMP)||LA277_0==NEWKW||(LA277_0>=EVAL_AND_EXPR && LA277_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA277_0==EVENT_PROP_EXPR||LA277_0==CONCAT||(LA277_0>=LIB_FUNC_CHAIN && LA277_0<=DOT_EXPR)||LA277_0==ARRAY_EXPR||(LA277_0>=NOT_IN_SET && LA277_0<=NOT_REGEXP)||(LA277_0>=IN_RANGE && LA277_0<=SUBSELECT_EXPR)||(LA277_0>=EXISTS_SUBSELECT_EXPR && LA277_0<=NOT_IN_SUBSELECT_EXPR)||LA277_0==SUBSTITUTION||(LA277_0>=FIRST_AGGREG && LA277_0<=WINDOW_AGGREG)||(LA277_0>=INT_TYPE && LA277_0<=NULL_TYPE)||(LA277_0>=JSON_OBJECT && LA277_0<=JSON_ARRAY)||LA277_0==STAR||(LA277_0>=LT && LA277_0<=GT)||(LA277_0>=BOR && LA277_0<=PLUS)||(LA277_0>=BAND && LA277_0<=BXOR)||(LA277_0>=LE && LA277_0<=GE)||(LA277_0>=MINUS && LA277_0<=MOD)||(LA277_0>=EVAL_IS_GROUP_EXPR && LA277_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt277=1;
            }
            switch (alt277) {
                case 1 :
                    // EsperEPL2Ast.g:779:40: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_newAssign5418);
                    valueExpr();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "newAssign"


    // $ANTLR start "libFuncChain"
    // EsperEPL2Ast.g:782:1: libFuncChain : ^(l= LIB_FUNC_CHAIN libFunctionWithClass ( libOrPropFunction )* ) ;
    public final void libFuncChain() throws RecognitionException {
        CommonTree l=null;

        try {
            // EsperEPL2Ast.g:783:2: ( ^(l= LIB_FUNC_CHAIN libFunctionWithClass ( libOrPropFunction )* ) )
            // EsperEPL2Ast.g:783:6: ^(l= LIB_FUNC_CHAIN libFunctionWithClass ( libOrPropFunction )* )
            {
            l=(CommonTree)match(input,LIB_FUNC_CHAIN,FOLLOW_LIB_FUNC_CHAIN_in_libFuncChain5436); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_libFunctionWithClass_in_libFuncChain5438);
            libFunctionWithClass();

            state._fsp--;

            // EsperEPL2Ast.g:783:46: ( libOrPropFunction )*
            loop278:
            do {
                int alt278=2;
                int LA278_0 = input.LA(1);

                if ( (LA278_0==EVENT_PROP_EXPR||LA278_0==LIB_FUNCTION) ) {
                    alt278=1;
                }


                switch (alt278) {
            	case 1 :
            	    // EsperEPL2Ast.g:783:46: libOrPropFunction
            	    {
            	    pushFollow(FOLLOW_libOrPropFunction_in_libFuncChain5440);
            	    libOrPropFunction();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop278;
                }
            } while (true);


            match(input, Token.UP, null); 
             leaveNode(l); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "libFuncChain"


    // $ANTLR start "libFunctionWithClass"
    // EsperEPL2Ast.g:786:1: libFunctionWithClass : ^(l= LIB_FUNCTION ( CLASS_IDENT )? IDENT ( DISTINCT )? ( libFunctionArgItem )* ( LPAREN )? ) ;
    public final void libFunctionWithClass() throws RecognitionException {
        CommonTree l=null;

        try {
            // EsperEPL2Ast.g:787:2: ( ^(l= LIB_FUNCTION ( CLASS_IDENT )? IDENT ( DISTINCT )? ( libFunctionArgItem )* ( LPAREN )? ) )
            // EsperEPL2Ast.g:787:6: ^(l= LIB_FUNCTION ( CLASS_IDENT )? IDENT ( DISTINCT )? ( libFunctionArgItem )* ( LPAREN )? )
            {
            l=(CommonTree)match(input,LIB_FUNCTION,FOLLOW_LIB_FUNCTION_in_libFunctionWithClass5460); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:787:23: ( CLASS_IDENT )?
            int alt279=2;
            int LA279_0 = input.LA(1);

            if ( (LA279_0==CLASS_IDENT) ) {
                alt279=1;
            }
            switch (alt279) {
                case 1 :
                    // EsperEPL2Ast.g:787:24: CLASS_IDENT
                    {
                    match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_libFunctionWithClass5463); 

                    }
                    break;

            }

            match(input,IDENT,FOLLOW_IDENT_in_libFunctionWithClass5467); 
            // EsperEPL2Ast.g:787:44: ( DISTINCT )?
            int alt280=2;
            int LA280_0 = input.LA(1);

            if ( (LA280_0==DISTINCT) ) {
                alt280=1;
            }
            switch (alt280) {
                case 1 :
                    // EsperEPL2Ast.g:787:45: DISTINCT
                    {
                    match(input,DISTINCT,FOLLOW_DISTINCT_in_libFunctionWithClass5470); 

                    }
                    break;

            }

            // EsperEPL2Ast.g:787:56: ( libFunctionArgItem )*
            loop281:
            do {
                int alt281=2;
                int LA281_0 = input.LA(1);

                if ( ((LA281_0>=IN_SET && LA281_0<=REGEXP)||LA281_0==NOT_EXPR||(LA281_0>=SUM && LA281_0<=AVG)||(LA281_0>=COALESCE && LA281_0<=COUNT)||(LA281_0>=CASE && LA281_0<=CASE2)||LA281_0==LAST||LA281_0==ISTREAM||(LA281_0>=PREVIOUS && LA281_0<=EXISTS)||(LA281_0>=LW && LA281_0<=CURRENT_TIMESTAMP)||LA281_0==NEWKW||(LA281_0>=NUMERIC_PARAM_RANGE && LA281_0<=OBJECT_PARAM_ORDERED_EXPR)||(LA281_0>=EVAL_AND_EXPR && LA281_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA281_0==EVENT_PROP_EXPR||LA281_0==CONCAT||(LA281_0>=LIB_FUNC_CHAIN && LA281_0<=DOT_EXPR)||(LA281_0>=TIME_PERIOD && LA281_0<=ARRAY_EXPR)||(LA281_0>=NOT_IN_SET && LA281_0<=NOT_REGEXP)||(LA281_0>=IN_RANGE && LA281_0<=SUBSELECT_EXPR)||(LA281_0>=EXISTS_SUBSELECT_EXPR && LA281_0<=NOT_IN_SUBSELECT_EXPR)||(LA281_0>=LAST_OPERATOR && LA281_0<=SUBSTITUTION)||LA281_0==NUMBERSETSTAR||(LA281_0>=FIRST_AGGREG && LA281_0<=WINDOW_AGGREG)||(LA281_0>=INT_TYPE && LA281_0<=NULL_TYPE)||(LA281_0>=JSON_OBJECT && LA281_0<=JSON_ARRAY)||LA281_0==GOES||LA281_0==STAR||(LA281_0>=LT && LA281_0<=GT)||(LA281_0>=BOR && LA281_0<=PLUS)||(LA281_0>=BAND && LA281_0<=BXOR)||(LA281_0>=LE && LA281_0<=GE)||(LA281_0>=MINUS && LA281_0<=MOD)||(LA281_0>=EVAL_IS_GROUP_EXPR && LA281_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt281=1;
                }


                switch (alt281) {
            	case 1 :
            	    // EsperEPL2Ast.g:787:56: libFunctionArgItem
            	    {
            	    pushFollow(FOLLOW_libFunctionArgItem_in_libFunctionWithClass5474);
            	    libFunctionArgItem();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop281;
                }
            } while (true);

            // EsperEPL2Ast.g:787:76: ( LPAREN )?
            int alt282=2;
            int LA282_0 = input.LA(1);

            if ( (LA282_0==LPAREN) ) {
                alt282=1;
            }
            switch (alt282) {
                case 1 :
                    // EsperEPL2Ast.g:787:76: LPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_libFunctionWithClass5477); 

                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "libFunctionWithClass"


    // $ANTLR start "libFunctionArgItem"
    // EsperEPL2Ast.g:790:1: libFunctionArgItem : ( expressionLambdaDecl | valueExprWithTime );
    public final void libFunctionArgItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:791:2: ( expressionLambdaDecl | valueExprWithTime )
            int alt283=2;
            int LA283_0 = input.LA(1);

            if ( (LA283_0==GOES) ) {
                alt283=1;
            }
            else if ( ((LA283_0>=IN_SET && LA283_0<=REGEXP)||LA283_0==NOT_EXPR||(LA283_0>=SUM && LA283_0<=AVG)||(LA283_0>=COALESCE && LA283_0<=COUNT)||(LA283_0>=CASE && LA283_0<=CASE2)||LA283_0==LAST||LA283_0==ISTREAM||(LA283_0>=PREVIOUS && LA283_0<=EXISTS)||(LA283_0>=LW && LA283_0<=CURRENT_TIMESTAMP)||LA283_0==NEWKW||(LA283_0>=NUMERIC_PARAM_RANGE && LA283_0<=OBJECT_PARAM_ORDERED_EXPR)||(LA283_0>=EVAL_AND_EXPR && LA283_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA283_0==EVENT_PROP_EXPR||LA283_0==CONCAT||(LA283_0>=LIB_FUNC_CHAIN && LA283_0<=DOT_EXPR)||(LA283_0>=TIME_PERIOD && LA283_0<=ARRAY_EXPR)||(LA283_0>=NOT_IN_SET && LA283_0<=NOT_REGEXP)||(LA283_0>=IN_RANGE && LA283_0<=SUBSELECT_EXPR)||(LA283_0>=EXISTS_SUBSELECT_EXPR && LA283_0<=NOT_IN_SUBSELECT_EXPR)||(LA283_0>=LAST_OPERATOR && LA283_0<=SUBSTITUTION)||LA283_0==NUMBERSETSTAR||(LA283_0>=FIRST_AGGREG && LA283_0<=WINDOW_AGGREG)||(LA283_0>=INT_TYPE && LA283_0<=NULL_TYPE)||(LA283_0>=JSON_OBJECT && LA283_0<=JSON_ARRAY)||LA283_0==STAR||(LA283_0>=LT && LA283_0<=GT)||(LA283_0>=BOR && LA283_0<=PLUS)||(LA283_0>=BAND && LA283_0<=BXOR)||(LA283_0>=LE && LA283_0<=GE)||(LA283_0>=MINUS && LA283_0<=MOD)||(LA283_0>=EVAL_IS_GROUP_EXPR && LA283_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt283=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 283, 0, input);

                throw nvae;
            }
            switch (alt283) {
                case 1 :
                    // EsperEPL2Ast.g:791:4: expressionLambdaDecl
                    {
                    pushFollow(FOLLOW_expressionLambdaDecl_in_libFunctionArgItem5491);
                    expressionLambdaDecl();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:791:27: valueExprWithTime
                    {
                    pushFollow(FOLLOW_valueExprWithTime_in_libFunctionArgItem5495);
                    valueExprWithTime();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "libFunctionArgItem"


    // $ANTLR start "libOrPropFunction"
    // EsperEPL2Ast.g:794:1: libOrPropFunction : ( eventPropertyExpr[false] | libFunctionWithClass );
    public final void libOrPropFunction() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:795:2: ( eventPropertyExpr[false] | libFunctionWithClass )
            int alt284=2;
            int LA284_0 = input.LA(1);

            if ( (LA284_0==EVENT_PROP_EXPR) ) {
                alt284=1;
            }
            else if ( (LA284_0==LIB_FUNCTION) ) {
                alt284=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 284, 0, input);

                throw nvae;
            }
            switch (alt284) {
                case 1 :
                    // EsperEPL2Ast.g:795:7: eventPropertyExpr[false]
                    {
                    pushFollow(FOLLOW_eventPropertyExpr_in_libOrPropFunction5510);
                    eventPropertyExpr(false);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:796:7: libFunctionWithClass
                    {
                    pushFollow(FOLLOW_libFunctionWithClass_in_libOrPropFunction5520);
                    libFunctionWithClass();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "libOrPropFunction"


    // $ANTLR start "startPatternExpressionRule"
    // EsperEPL2Ast.g:802:1: startPatternExpressionRule : ( annotation[true] )* exprChoice ;
    public final void startPatternExpressionRule() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:803:2: ( ( annotation[true] )* exprChoice )
            // EsperEPL2Ast.g:803:4: ( annotation[true] )* exprChoice
            {
            // EsperEPL2Ast.g:803:4: ( annotation[true] )*
            loop285:
            do {
                int alt285=2;
                int LA285_0 = input.LA(1);

                if ( (LA285_0==ANNOTATION) ) {
                    alt285=1;
                }


                switch (alt285) {
            	case 1 :
            	    // EsperEPL2Ast.g:803:4: annotation[true]
            	    {
            	    pushFollow(FOLLOW_annotation_in_startPatternExpressionRule5535);
            	    annotation(true);

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop285;
                }
            } while (true);

            pushFollow(FOLLOW_exprChoice_in_startPatternExpressionRule5539);
            exprChoice();

            state._fsp--;

             endPattern(); end(); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "startPatternExpressionRule"


    // $ANTLR start "exprChoice"
    // EsperEPL2Ast.g:806:1: exprChoice : ( atomicExpr | patternOp | ^(a= EVERY_EXPR exprChoice ) | ^(a= EVERY_DISTINCT_EXPR distinctExpressions exprChoice ) | ^(n= PATTERN_NOT_EXPR exprChoice ) | ^(g= GUARD_EXPR exprChoice ( IDENT IDENT ( valueExprWithTime )* | valueExpr ) ) | ^(m= MATCH_UNTIL_EXPR ( matchUntilRange )? exprChoice ( exprChoice )? ) );
    public final void exprChoice() throws RecognitionException {
        CommonTree a=null;
        CommonTree n=null;
        CommonTree g=null;
        CommonTree m=null;

        try {
            // EsperEPL2Ast.g:807:2: ( atomicExpr | patternOp | ^(a= EVERY_EXPR exprChoice ) | ^(a= EVERY_DISTINCT_EXPR distinctExpressions exprChoice ) | ^(n= PATTERN_NOT_EXPR exprChoice ) | ^(g= GUARD_EXPR exprChoice ( IDENT IDENT ( valueExprWithTime )* | valueExpr ) ) | ^(m= MATCH_UNTIL_EXPR ( matchUntilRange )? exprChoice ( exprChoice )? ) )
            int alt290=7;
            switch ( input.LA(1) ) {
            case PATTERN_FILTER_EXPR:
            case OBSERVER_EXPR:
                {
                alt290=1;
                }
                break;
            case OR_EXPR:
            case AND_EXPR:
            case FOLLOWED_BY_EXPR:
                {
                alt290=2;
                }
                break;
            case EVERY_EXPR:
                {
                alt290=3;
                }
                break;
            case EVERY_DISTINCT_EXPR:
                {
                alt290=4;
                }
                break;
            case PATTERN_NOT_EXPR:
                {
                alt290=5;
                }
                break;
            case GUARD_EXPR:
                {
                alt290=6;
                }
                break;
            case MATCH_UNTIL_EXPR:
                {
                alt290=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 290, 0, input);

                throw nvae;
            }

            switch (alt290) {
                case 1 :
                    // EsperEPL2Ast.g:807:5: atomicExpr
                    {
                    pushFollow(FOLLOW_atomicExpr_in_exprChoice5553);
                    atomicExpr();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:808:4: patternOp
                    {
                    pushFollow(FOLLOW_patternOp_in_exprChoice5558);
                    patternOp();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:809:5: ^(a= EVERY_EXPR exprChoice )
                    {
                    a=(CommonTree)match(input,EVERY_EXPR,FOLLOW_EVERY_EXPR_in_exprChoice5568); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_exprChoice_in_exprChoice5570);
                    exprChoice();

                    state._fsp--;

                     leaveNode(a); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:810:5: ^(a= EVERY_DISTINCT_EXPR distinctExpressions exprChoice )
                    {
                    a=(CommonTree)match(input,EVERY_DISTINCT_EXPR,FOLLOW_EVERY_DISTINCT_EXPR_in_exprChoice5584); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_distinctExpressions_in_exprChoice5586);
                    distinctExpressions();

                    state._fsp--;

                    pushFollow(FOLLOW_exprChoice_in_exprChoice5588);
                    exprChoice();

                    state._fsp--;

                     leaveNode(a); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:811:5: ^(n= PATTERN_NOT_EXPR exprChoice )
                    {
                    n=(CommonTree)match(input,PATTERN_NOT_EXPR,FOLLOW_PATTERN_NOT_EXPR_in_exprChoice5602); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_exprChoice_in_exprChoice5604);
                    exprChoice();

                    state._fsp--;

                     leaveNode(n); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:812:5: ^(g= GUARD_EXPR exprChoice ( IDENT IDENT ( valueExprWithTime )* | valueExpr ) )
                    {
                    g=(CommonTree)match(input,GUARD_EXPR,FOLLOW_GUARD_EXPR_in_exprChoice5618); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_exprChoice_in_exprChoice5620);
                    exprChoice();

                    state._fsp--;

                    // EsperEPL2Ast.g:812:32: ( IDENT IDENT ( valueExprWithTime )* | valueExpr )
                    int alt287=2;
                    int LA287_0 = input.LA(1);

                    if ( (LA287_0==IDENT) ) {
                        alt287=1;
                    }
                    else if ( ((LA287_0>=IN_SET && LA287_0<=REGEXP)||LA287_0==NOT_EXPR||(LA287_0>=SUM && LA287_0<=AVG)||(LA287_0>=COALESCE && LA287_0<=COUNT)||(LA287_0>=CASE && LA287_0<=CASE2)||LA287_0==ISTREAM||(LA287_0>=PREVIOUS && LA287_0<=EXISTS)||(LA287_0>=INSTANCEOF && LA287_0<=CURRENT_TIMESTAMP)||LA287_0==NEWKW||(LA287_0>=EVAL_AND_EXPR && LA287_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA287_0==EVENT_PROP_EXPR||LA287_0==CONCAT||(LA287_0>=LIB_FUNC_CHAIN && LA287_0<=DOT_EXPR)||LA287_0==ARRAY_EXPR||(LA287_0>=NOT_IN_SET && LA287_0<=NOT_REGEXP)||(LA287_0>=IN_RANGE && LA287_0<=SUBSELECT_EXPR)||(LA287_0>=EXISTS_SUBSELECT_EXPR && LA287_0<=NOT_IN_SUBSELECT_EXPR)||LA287_0==SUBSTITUTION||(LA287_0>=FIRST_AGGREG && LA287_0<=WINDOW_AGGREG)||(LA287_0>=INT_TYPE && LA287_0<=NULL_TYPE)||(LA287_0>=JSON_OBJECT && LA287_0<=JSON_ARRAY)||LA287_0==STAR||(LA287_0>=LT && LA287_0<=GT)||(LA287_0>=BOR && LA287_0<=PLUS)||(LA287_0>=BAND && LA287_0<=BXOR)||(LA287_0>=LE && LA287_0<=GE)||(LA287_0>=MINUS && LA287_0<=MOD)||(LA287_0>=EVAL_IS_GROUP_EXPR && LA287_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                        alt287=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 287, 0, input);

                        throw nvae;
                    }
                    switch (alt287) {
                        case 1 :
                            // EsperEPL2Ast.g:812:33: IDENT IDENT ( valueExprWithTime )*
                            {
                            match(input,IDENT,FOLLOW_IDENT_in_exprChoice5623); 
                            match(input,IDENT,FOLLOW_IDENT_in_exprChoice5625); 
                            // EsperEPL2Ast.g:812:45: ( valueExprWithTime )*
                            loop286:
                            do {
                                int alt286=2;
                                int LA286_0 = input.LA(1);

                                if ( ((LA286_0>=IN_SET && LA286_0<=REGEXP)||LA286_0==NOT_EXPR||(LA286_0>=SUM && LA286_0<=AVG)||(LA286_0>=COALESCE && LA286_0<=COUNT)||(LA286_0>=CASE && LA286_0<=CASE2)||LA286_0==LAST||LA286_0==ISTREAM||(LA286_0>=PREVIOUS && LA286_0<=EXISTS)||(LA286_0>=LW && LA286_0<=CURRENT_TIMESTAMP)||LA286_0==NEWKW||(LA286_0>=NUMERIC_PARAM_RANGE && LA286_0<=OBJECT_PARAM_ORDERED_EXPR)||(LA286_0>=EVAL_AND_EXPR && LA286_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA286_0==EVENT_PROP_EXPR||LA286_0==CONCAT||(LA286_0>=LIB_FUNC_CHAIN && LA286_0<=DOT_EXPR)||(LA286_0>=TIME_PERIOD && LA286_0<=ARRAY_EXPR)||(LA286_0>=NOT_IN_SET && LA286_0<=NOT_REGEXP)||(LA286_0>=IN_RANGE && LA286_0<=SUBSELECT_EXPR)||(LA286_0>=EXISTS_SUBSELECT_EXPR && LA286_0<=NOT_IN_SUBSELECT_EXPR)||(LA286_0>=LAST_OPERATOR && LA286_0<=SUBSTITUTION)||LA286_0==NUMBERSETSTAR||(LA286_0>=FIRST_AGGREG && LA286_0<=WINDOW_AGGREG)||(LA286_0>=INT_TYPE && LA286_0<=NULL_TYPE)||(LA286_0>=JSON_OBJECT && LA286_0<=JSON_ARRAY)||LA286_0==STAR||(LA286_0>=LT && LA286_0<=GT)||(LA286_0>=BOR && LA286_0<=PLUS)||(LA286_0>=BAND && LA286_0<=BXOR)||(LA286_0>=LE && LA286_0<=GE)||(LA286_0>=MINUS && LA286_0<=MOD)||(LA286_0>=EVAL_IS_GROUP_EXPR && LA286_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                                    alt286=1;
                                }


                                switch (alt286) {
                            	case 1 :
                            	    // EsperEPL2Ast.g:812:45: valueExprWithTime
                            	    {
                            	    pushFollow(FOLLOW_valueExprWithTime_in_exprChoice5627);
                            	    valueExprWithTime();

                            	    state._fsp--;


                            	    }
                            	    break;

                            	default :
                            	    break loop286;
                                }
                            } while (true);


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:812:66: valueExpr
                            {
                            pushFollow(FOLLOW_valueExpr_in_exprChoice5632);
                            valueExpr();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(g); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:813:4: ^(m= MATCH_UNTIL_EXPR ( matchUntilRange )? exprChoice ( exprChoice )? )
                    {
                    m=(CommonTree)match(input,MATCH_UNTIL_EXPR,FOLLOW_MATCH_UNTIL_EXPR_in_exprChoice5646); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:813:26: ( matchUntilRange )?
                    int alt288=2;
                    int LA288_0 = input.LA(1);

                    if ( ((LA288_0>=MATCH_UNTIL_RANGE_HALFOPEN && LA288_0<=MATCH_UNTIL_RANGE_BOUNDED)) ) {
                        alt288=1;
                    }
                    switch (alt288) {
                        case 1 :
                            // EsperEPL2Ast.g:813:26: matchUntilRange
                            {
                            pushFollow(FOLLOW_matchUntilRange_in_exprChoice5648);
                            matchUntilRange();

                            state._fsp--;


                            }
                            break;

                    }

                    pushFollow(FOLLOW_exprChoice_in_exprChoice5651);
                    exprChoice();

                    state._fsp--;

                    // EsperEPL2Ast.g:813:54: ( exprChoice )?
                    int alt289=2;
                    int LA289_0 = input.LA(1);

                    if ( ((LA289_0>=OR_EXPR && LA289_0<=AND_EXPR)||(LA289_0>=EVERY_EXPR && LA289_0<=EVERY_DISTINCT_EXPR)||LA289_0==FOLLOWED_BY_EXPR||(LA289_0>=PATTERN_FILTER_EXPR && LA289_0<=PATTERN_NOT_EXPR)||(LA289_0>=GUARD_EXPR && LA289_0<=OBSERVER_EXPR)||LA289_0==MATCH_UNTIL_EXPR) ) {
                        alt289=1;
                    }
                    switch (alt289) {
                        case 1 :
                            // EsperEPL2Ast.g:813:54: exprChoice
                            {
                            pushFollow(FOLLOW_exprChoice_in_exprChoice5653);
                            exprChoice();

                            state._fsp--;


                            }
                            break;

                    }

                     leaveNode(m); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "exprChoice"


    // $ANTLR start "distinctExpressions"
    // EsperEPL2Ast.g:817:1: distinctExpressions : ^( PATTERN_EVERY_DISTINCT_EXPR ( valueExprWithTime )+ ) ;
    public final void distinctExpressions() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:818:2: ( ^( PATTERN_EVERY_DISTINCT_EXPR ( valueExprWithTime )+ ) )
            // EsperEPL2Ast.g:818:4: ^( PATTERN_EVERY_DISTINCT_EXPR ( valueExprWithTime )+ )
            {
            match(input,PATTERN_EVERY_DISTINCT_EXPR,FOLLOW_PATTERN_EVERY_DISTINCT_EXPR_in_distinctExpressions5674); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:818:35: ( valueExprWithTime )+
            int cnt291=0;
            loop291:
            do {
                int alt291=2;
                int LA291_0 = input.LA(1);

                if ( ((LA291_0>=IN_SET && LA291_0<=REGEXP)||LA291_0==NOT_EXPR||(LA291_0>=SUM && LA291_0<=AVG)||(LA291_0>=COALESCE && LA291_0<=COUNT)||(LA291_0>=CASE && LA291_0<=CASE2)||LA291_0==LAST||LA291_0==ISTREAM||(LA291_0>=PREVIOUS && LA291_0<=EXISTS)||(LA291_0>=LW && LA291_0<=CURRENT_TIMESTAMP)||LA291_0==NEWKW||(LA291_0>=NUMERIC_PARAM_RANGE && LA291_0<=OBJECT_PARAM_ORDERED_EXPR)||(LA291_0>=EVAL_AND_EXPR && LA291_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA291_0==EVENT_PROP_EXPR||LA291_0==CONCAT||(LA291_0>=LIB_FUNC_CHAIN && LA291_0<=DOT_EXPR)||(LA291_0>=TIME_PERIOD && LA291_0<=ARRAY_EXPR)||(LA291_0>=NOT_IN_SET && LA291_0<=NOT_REGEXP)||(LA291_0>=IN_RANGE && LA291_0<=SUBSELECT_EXPR)||(LA291_0>=EXISTS_SUBSELECT_EXPR && LA291_0<=NOT_IN_SUBSELECT_EXPR)||(LA291_0>=LAST_OPERATOR && LA291_0<=SUBSTITUTION)||LA291_0==NUMBERSETSTAR||(LA291_0>=FIRST_AGGREG && LA291_0<=WINDOW_AGGREG)||(LA291_0>=INT_TYPE && LA291_0<=NULL_TYPE)||(LA291_0>=JSON_OBJECT && LA291_0<=JSON_ARRAY)||LA291_0==STAR||(LA291_0>=LT && LA291_0<=GT)||(LA291_0>=BOR && LA291_0<=PLUS)||(LA291_0>=BAND && LA291_0<=BXOR)||(LA291_0>=LE && LA291_0<=GE)||(LA291_0>=MINUS && LA291_0<=MOD)||(LA291_0>=EVAL_IS_GROUP_EXPR && LA291_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt291=1;
                }


                switch (alt291) {
            	case 1 :
            	    // EsperEPL2Ast.g:818:35: valueExprWithTime
            	    {
            	    pushFollow(FOLLOW_valueExprWithTime_in_distinctExpressions5676);
            	    valueExprWithTime();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt291 >= 1 ) break loop291;
                        EarlyExitException eee =
                            new EarlyExitException(291, input);
                        throw eee;
                }
                cnt291++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "distinctExpressions"


    // $ANTLR start "patternOp"
    // EsperEPL2Ast.g:821:1: patternOp : ( ^(f= FOLLOWED_BY_EXPR followedByItem followedByItem ( followedByItem )* ) | ^(o= OR_EXPR exprChoice exprChoice ( exprChoice )* ) | ^(a= AND_EXPR exprChoice exprChoice ( exprChoice )* ) );
    public final void patternOp() throws RecognitionException {
        CommonTree f=null;
        CommonTree o=null;
        CommonTree a=null;

        try {
            // EsperEPL2Ast.g:822:2: ( ^(f= FOLLOWED_BY_EXPR followedByItem followedByItem ( followedByItem )* ) | ^(o= OR_EXPR exprChoice exprChoice ( exprChoice )* ) | ^(a= AND_EXPR exprChoice exprChoice ( exprChoice )* ) )
            int alt295=3;
            switch ( input.LA(1) ) {
            case FOLLOWED_BY_EXPR:
                {
                alt295=1;
                }
                break;
            case OR_EXPR:
                {
                alt295=2;
                }
                break;
            case AND_EXPR:
                {
                alt295=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 295, 0, input);

                throw nvae;
            }

            switch (alt295) {
                case 1 :
                    // EsperEPL2Ast.g:822:4: ^(f= FOLLOWED_BY_EXPR followedByItem followedByItem ( followedByItem )* )
                    {
                    f=(CommonTree)match(input,FOLLOWED_BY_EXPR,FOLLOW_FOLLOWED_BY_EXPR_in_patternOp5695); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_followedByItem_in_patternOp5697);
                    followedByItem();

                    state._fsp--;

                    pushFollow(FOLLOW_followedByItem_in_patternOp5699);
                    followedByItem();

                    state._fsp--;

                    // EsperEPL2Ast.g:822:56: ( followedByItem )*
                    loop292:
                    do {
                        int alt292=2;
                        int LA292_0 = input.LA(1);

                        if ( (LA292_0==FOLLOWED_BY_ITEM) ) {
                            alt292=1;
                        }


                        switch (alt292) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:822:57: followedByItem
                    	    {
                    	    pushFollow(FOLLOW_followedByItem_in_patternOp5702);
                    	    followedByItem();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop292;
                        }
                    } while (true);

                     leaveNode(f); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:823:5: ^(o= OR_EXPR exprChoice exprChoice ( exprChoice )* )
                    {
                    o=(CommonTree)match(input,OR_EXPR,FOLLOW_OR_EXPR_in_patternOp5718); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_exprChoice_in_patternOp5720);
                    exprChoice();

                    state._fsp--;

                    pushFollow(FOLLOW_exprChoice_in_patternOp5722);
                    exprChoice();

                    state._fsp--;

                    // EsperEPL2Ast.g:823:40: ( exprChoice )*
                    loop293:
                    do {
                        int alt293=2;
                        int LA293_0 = input.LA(1);

                        if ( ((LA293_0>=OR_EXPR && LA293_0<=AND_EXPR)||(LA293_0>=EVERY_EXPR && LA293_0<=EVERY_DISTINCT_EXPR)||LA293_0==FOLLOWED_BY_EXPR||(LA293_0>=PATTERN_FILTER_EXPR && LA293_0<=PATTERN_NOT_EXPR)||(LA293_0>=GUARD_EXPR && LA293_0<=OBSERVER_EXPR)||LA293_0==MATCH_UNTIL_EXPR) ) {
                            alt293=1;
                        }


                        switch (alt293) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:823:41: exprChoice
                    	    {
                    	    pushFollow(FOLLOW_exprChoice_in_patternOp5725);
                    	    exprChoice();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop293;
                        }
                    } while (true);

                     leaveNode(o); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:824:5: ^(a= AND_EXPR exprChoice exprChoice ( exprChoice )* )
                    {
                    a=(CommonTree)match(input,AND_EXPR,FOLLOW_AND_EXPR_in_patternOp5741); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_exprChoice_in_patternOp5743);
                    exprChoice();

                    state._fsp--;

                    pushFollow(FOLLOW_exprChoice_in_patternOp5745);
                    exprChoice();

                    state._fsp--;

                    // EsperEPL2Ast.g:824:41: ( exprChoice )*
                    loop294:
                    do {
                        int alt294=2;
                        int LA294_0 = input.LA(1);

                        if ( ((LA294_0>=OR_EXPR && LA294_0<=AND_EXPR)||(LA294_0>=EVERY_EXPR && LA294_0<=EVERY_DISTINCT_EXPR)||LA294_0==FOLLOWED_BY_EXPR||(LA294_0>=PATTERN_FILTER_EXPR && LA294_0<=PATTERN_NOT_EXPR)||(LA294_0>=GUARD_EXPR && LA294_0<=OBSERVER_EXPR)||LA294_0==MATCH_UNTIL_EXPR) ) {
                            alt294=1;
                        }


                        switch (alt294) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:824:42: exprChoice
                    	    {
                    	    pushFollow(FOLLOW_exprChoice_in_patternOp5748);
                    	    exprChoice();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop294;
                        }
                    } while (true);

                     leaveNode(a); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "patternOp"


    // $ANTLR start "followedByItem"
    // EsperEPL2Ast.g:827:1: followedByItem : ^( FOLLOWED_BY_ITEM ( valueExpr )? exprChoice ) ;
    public final void followedByItem() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:828:2: ( ^( FOLLOWED_BY_ITEM ( valueExpr )? exprChoice ) )
            // EsperEPL2Ast.g:828:4: ^( FOLLOWED_BY_ITEM ( valueExpr )? exprChoice )
            {
            match(input,FOLLOWED_BY_ITEM,FOLLOW_FOLLOWED_BY_ITEM_in_followedByItem5769); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:828:24: ( valueExpr )?
            int alt296=2;
            int LA296_0 = input.LA(1);

            if ( ((LA296_0>=IN_SET && LA296_0<=REGEXP)||LA296_0==NOT_EXPR||(LA296_0>=SUM && LA296_0<=AVG)||(LA296_0>=COALESCE && LA296_0<=COUNT)||(LA296_0>=CASE && LA296_0<=CASE2)||LA296_0==ISTREAM||(LA296_0>=PREVIOUS && LA296_0<=EXISTS)||(LA296_0>=INSTANCEOF && LA296_0<=CURRENT_TIMESTAMP)||LA296_0==NEWKW||(LA296_0>=EVAL_AND_EXPR && LA296_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA296_0==EVENT_PROP_EXPR||LA296_0==CONCAT||(LA296_0>=LIB_FUNC_CHAIN && LA296_0<=DOT_EXPR)||LA296_0==ARRAY_EXPR||(LA296_0>=NOT_IN_SET && LA296_0<=NOT_REGEXP)||(LA296_0>=IN_RANGE && LA296_0<=SUBSELECT_EXPR)||(LA296_0>=EXISTS_SUBSELECT_EXPR && LA296_0<=NOT_IN_SUBSELECT_EXPR)||LA296_0==SUBSTITUTION||(LA296_0>=FIRST_AGGREG && LA296_0<=WINDOW_AGGREG)||(LA296_0>=INT_TYPE && LA296_0<=NULL_TYPE)||(LA296_0>=JSON_OBJECT && LA296_0<=JSON_ARRAY)||LA296_0==STAR||(LA296_0>=LT && LA296_0<=GT)||(LA296_0>=BOR && LA296_0<=PLUS)||(LA296_0>=BAND && LA296_0<=BXOR)||(LA296_0>=LE && LA296_0<=GE)||(LA296_0>=MINUS && LA296_0<=MOD)||(LA296_0>=EVAL_IS_GROUP_EXPR && LA296_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                alt296=1;
            }
            switch (alt296) {
                case 1 :
                    // EsperEPL2Ast.g:828:24: valueExpr
                    {
                    pushFollow(FOLLOW_valueExpr_in_followedByItem5771);
                    valueExpr();

                    state._fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_exprChoice_in_followedByItem5774);
            exprChoice();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "followedByItem"


    // $ANTLR start "atomicExpr"
    // EsperEPL2Ast.g:831:1: atomicExpr : ( patternFilterExpr | ^(ac= OBSERVER_EXPR IDENT IDENT ( valueExprWithTime )* ) );
    public final void atomicExpr() throws RecognitionException {
        CommonTree ac=null;

        try {
            // EsperEPL2Ast.g:832:2: ( patternFilterExpr | ^(ac= OBSERVER_EXPR IDENT IDENT ( valueExprWithTime )* ) )
            int alt298=2;
            int LA298_0 = input.LA(1);

            if ( (LA298_0==PATTERN_FILTER_EXPR) ) {
                alt298=1;
            }
            else if ( (LA298_0==OBSERVER_EXPR) ) {
                alt298=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 298, 0, input);

                throw nvae;
            }
            switch (alt298) {
                case 1 :
                    // EsperEPL2Ast.g:832:4: patternFilterExpr
                    {
                    pushFollow(FOLLOW_patternFilterExpr_in_atomicExpr5788);
                    patternFilterExpr();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:833:7: ^(ac= OBSERVER_EXPR IDENT IDENT ( valueExprWithTime )* )
                    {
                    ac=(CommonTree)match(input,OBSERVER_EXPR,FOLLOW_OBSERVER_EXPR_in_atomicExpr5800); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_atomicExpr5802); 
                    match(input,IDENT,FOLLOW_IDENT_in_atomicExpr5804); 
                    // EsperEPL2Ast.g:833:39: ( valueExprWithTime )*
                    loop297:
                    do {
                        int alt297=2;
                        int LA297_0 = input.LA(1);

                        if ( ((LA297_0>=IN_SET && LA297_0<=REGEXP)||LA297_0==NOT_EXPR||(LA297_0>=SUM && LA297_0<=AVG)||(LA297_0>=COALESCE && LA297_0<=COUNT)||(LA297_0>=CASE && LA297_0<=CASE2)||LA297_0==LAST||LA297_0==ISTREAM||(LA297_0>=PREVIOUS && LA297_0<=EXISTS)||(LA297_0>=LW && LA297_0<=CURRENT_TIMESTAMP)||LA297_0==NEWKW||(LA297_0>=NUMERIC_PARAM_RANGE && LA297_0<=OBJECT_PARAM_ORDERED_EXPR)||(LA297_0>=EVAL_AND_EXPR && LA297_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA297_0==EVENT_PROP_EXPR||LA297_0==CONCAT||(LA297_0>=LIB_FUNC_CHAIN && LA297_0<=DOT_EXPR)||(LA297_0>=TIME_PERIOD && LA297_0<=ARRAY_EXPR)||(LA297_0>=NOT_IN_SET && LA297_0<=NOT_REGEXP)||(LA297_0>=IN_RANGE && LA297_0<=SUBSELECT_EXPR)||(LA297_0>=EXISTS_SUBSELECT_EXPR && LA297_0<=NOT_IN_SUBSELECT_EXPR)||(LA297_0>=LAST_OPERATOR && LA297_0<=SUBSTITUTION)||LA297_0==NUMBERSETSTAR||(LA297_0>=FIRST_AGGREG && LA297_0<=WINDOW_AGGREG)||(LA297_0>=INT_TYPE && LA297_0<=NULL_TYPE)||(LA297_0>=JSON_OBJECT && LA297_0<=JSON_ARRAY)||LA297_0==STAR||(LA297_0>=LT && LA297_0<=GT)||(LA297_0>=BOR && LA297_0<=PLUS)||(LA297_0>=BAND && LA297_0<=BXOR)||(LA297_0>=LE && LA297_0<=GE)||(LA297_0>=MINUS && LA297_0<=MOD)||(LA297_0>=EVAL_IS_GROUP_EXPR && LA297_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                            alt297=1;
                        }


                        switch (alt297) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:833:39: valueExprWithTime
                    	    {
                    	    pushFollow(FOLLOW_valueExprWithTime_in_atomicExpr5806);
                    	    valueExprWithTime();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop297;
                        }
                    } while (true);

                     leaveNode(ac); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "atomicExpr"


    // $ANTLR start "patternFilterExpr"
    // EsperEPL2Ast.g:836:1: patternFilterExpr : ^(f= PATTERN_FILTER_EXPR ( IDENT )? CLASS_IDENT ( propertyExpression )? ( patternFilterAnno )? ( valueExpr )* ) ;
    public final void patternFilterExpr() throws RecognitionException {
        CommonTree f=null;

        try {
            // EsperEPL2Ast.g:837:2: ( ^(f= PATTERN_FILTER_EXPR ( IDENT )? CLASS_IDENT ( propertyExpression )? ( patternFilterAnno )? ( valueExpr )* ) )
            // EsperEPL2Ast.g:837:4: ^(f= PATTERN_FILTER_EXPR ( IDENT )? CLASS_IDENT ( propertyExpression )? ( patternFilterAnno )? ( valueExpr )* )
            {
            f=(CommonTree)match(input,PATTERN_FILTER_EXPR,FOLLOW_PATTERN_FILTER_EXPR_in_patternFilterExpr5826); 

            match(input, Token.DOWN, null); 
            // EsperEPL2Ast.g:837:29: ( IDENT )?
            int alt299=2;
            int LA299_0 = input.LA(1);

            if ( (LA299_0==IDENT) ) {
                alt299=1;
            }
            switch (alt299) {
                case 1 :
                    // EsperEPL2Ast.g:837:29: IDENT
                    {
                    match(input,IDENT,FOLLOW_IDENT_in_patternFilterExpr5828); 

                    }
                    break;

            }

            match(input,CLASS_IDENT,FOLLOW_CLASS_IDENT_in_patternFilterExpr5831); 
            // EsperEPL2Ast.g:837:48: ( propertyExpression )?
            int alt300=2;
            int LA300_0 = input.LA(1);

            if ( (LA300_0==EVENT_FILTER_PROPERTY_EXPR) ) {
                alt300=1;
            }
            switch (alt300) {
                case 1 :
                    // EsperEPL2Ast.g:837:48: propertyExpression
                    {
                    pushFollow(FOLLOW_propertyExpression_in_patternFilterExpr5833);
                    propertyExpression();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:837:68: ( patternFilterAnno )?
            int alt301=2;
            int LA301_0 = input.LA(1);

            if ( (LA301_0==ATCHAR) ) {
                alt301=1;
            }
            switch (alt301) {
                case 1 :
                    // EsperEPL2Ast.g:837:68: patternFilterAnno
                    {
                    pushFollow(FOLLOW_patternFilterAnno_in_patternFilterExpr5836);
                    patternFilterAnno();

                    state._fsp--;


                    }
                    break;

            }

            // EsperEPL2Ast.g:837:87: ( valueExpr )*
            loop302:
            do {
                int alt302=2;
                int LA302_0 = input.LA(1);

                if ( ((LA302_0>=IN_SET && LA302_0<=REGEXP)||LA302_0==NOT_EXPR||(LA302_0>=SUM && LA302_0<=AVG)||(LA302_0>=COALESCE && LA302_0<=COUNT)||(LA302_0>=CASE && LA302_0<=CASE2)||LA302_0==ISTREAM||(LA302_0>=PREVIOUS && LA302_0<=EXISTS)||(LA302_0>=INSTANCEOF && LA302_0<=CURRENT_TIMESTAMP)||LA302_0==NEWKW||(LA302_0>=EVAL_AND_EXPR && LA302_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA302_0==EVENT_PROP_EXPR||LA302_0==CONCAT||(LA302_0>=LIB_FUNC_CHAIN && LA302_0<=DOT_EXPR)||LA302_0==ARRAY_EXPR||(LA302_0>=NOT_IN_SET && LA302_0<=NOT_REGEXP)||(LA302_0>=IN_RANGE && LA302_0<=SUBSELECT_EXPR)||(LA302_0>=EXISTS_SUBSELECT_EXPR && LA302_0<=NOT_IN_SUBSELECT_EXPR)||LA302_0==SUBSTITUTION||(LA302_0>=FIRST_AGGREG && LA302_0<=WINDOW_AGGREG)||(LA302_0>=INT_TYPE && LA302_0<=NULL_TYPE)||(LA302_0>=JSON_OBJECT && LA302_0<=JSON_ARRAY)||LA302_0==STAR||(LA302_0>=LT && LA302_0<=GT)||(LA302_0>=BOR && LA302_0<=PLUS)||(LA302_0>=BAND && LA302_0<=BXOR)||(LA302_0>=LE && LA302_0<=GE)||(LA302_0>=MINUS && LA302_0<=MOD)||(LA302_0>=EVAL_IS_GROUP_EXPR && LA302_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt302=1;
                }


                switch (alt302) {
            	case 1 :
            	    // EsperEPL2Ast.g:837:88: valueExpr
            	    {
            	    pushFollow(FOLLOW_valueExpr_in_patternFilterExpr5840);
            	    valueExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop302;
                }
            } while (true);

             leaveNode(f); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "patternFilterExpr"


    // $ANTLR start "patternFilterAnno"
    // EsperEPL2Ast.g:840:1: patternFilterAnno : ^( ATCHAR IDENT ( number )? ) ;
    public final void patternFilterAnno() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:841:2: ( ^( ATCHAR IDENT ( number )? ) )
            // EsperEPL2Ast.g:841:4: ^( ATCHAR IDENT ( number )? )
            {
            match(input,ATCHAR,FOLLOW_ATCHAR_in_patternFilterAnno5860); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_patternFilterAnno5862); 
            // EsperEPL2Ast.g:841:20: ( number )?
            int alt303=2;
            int LA303_0 = input.LA(1);

            if ( ((LA303_0>=INT_TYPE && LA303_0<=DOUBLE_TYPE)) ) {
                alt303=1;
            }
            switch (alt303) {
                case 1 :
                    // EsperEPL2Ast.g:841:20: number
                    {
                    pushFollow(FOLLOW_number_in_patternFilterAnno5864);
                    number();

                    state._fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "patternFilterAnno"


    // $ANTLR start "matchUntilRange"
    // EsperEPL2Ast.g:844:1: matchUntilRange : ( ^( MATCH_UNTIL_RANGE_CLOSED valueExpr valueExpr ) | ^( MATCH_UNTIL_RANGE_BOUNDED valueExpr ) | ^( MATCH_UNTIL_RANGE_HALFCLOSED valueExpr ) | ^( MATCH_UNTIL_RANGE_HALFOPEN valueExpr ) );
    public final void matchUntilRange() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:845:2: ( ^( MATCH_UNTIL_RANGE_CLOSED valueExpr valueExpr ) | ^( MATCH_UNTIL_RANGE_BOUNDED valueExpr ) | ^( MATCH_UNTIL_RANGE_HALFCLOSED valueExpr ) | ^( MATCH_UNTIL_RANGE_HALFOPEN valueExpr ) )
            int alt304=4;
            switch ( input.LA(1) ) {
            case MATCH_UNTIL_RANGE_CLOSED:
                {
                alt304=1;
                }
                break;
            case MATCH_UNTIL_RANGE_BOUNDED:
                {
                alt304=2;
                }
                break;
            case MATCH_UNTIL_RANGE_HALFCLOSED:
                {
                alt304=3;
                }
                break;
            case MATCH_UNTIL_RANGE_HALFOPEN:
                {
                alt304=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 304, 0, input);

                throw nvae;
            }

            switch (alt304) {
                case 1 :
                    // EsperEPL2Ast.g:845:4: ^( MATCH_UNTIL_RANGE_CLOSED valueExpr valueExpr )
                    {
                    match(input,MATCH_UNTIL_RANGE_CLOSED,FOLLOW_MATCH_UNTIL_RANGE_CLOSED_in_matchUntilRange5879); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_matchUntilRange5881);
                    valueExpr();

                    state._fsp--;

                    pushFollow(FOLLOW_valueExpr_in_matchUntilRange5883);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:846:5: ^( MATCH_UNTIL_RANGE_BOUNDED valueExpr )
                    {
                    match(input,MATCH_UNTIL_RANGE_BOUNDED,FOLLOW_MATCH_UNTIL_RANGE_BOUNDED_in_matchUntilRange5891); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_matchUntilRange5893);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:847:5: ^( MATCH_UNTIL_RANGE_HALFCLOSED valueExpr )
                    {
                    match(input,MATCH_UNTIL_RANGE_HALFCLOSED,FOLLOW_MATCH_UNTIL_RANGE_HALFCLOSED_in_matchUntilRange5901); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_matchUntilRange5903);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:848:4: ^( MATCH_UNTIL_RANGE_HALFOPEN valueExpr )
                    {
                    match(input,MATCH_UNTIL_RANGE_HALFOPEN,FOLLOW_MATCH_UNTIL_RANGE_HALFOPEN_in_matchUntilRange5910); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_valueExpr_in_matchUntilRange5912);
                    valueExpr();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "matchUntilRange"


    // $ANTLR start "filterParam"
    // EsperEPL2Ast.g:851:1: filterParam : ^( EVENT_FILTER_PARAM valueExpr ( valueExpr )* ) ;
    public final void filterParam() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:852:2: ( ^( EVENT_FILTER_PARAM valueExpr ( valueExpr )* ) )
            // EsperEPL2Ast.g:852:4: ^( EVENT_FILTER_PARAM valueExpr ( valueExpr )* )
            {
            match(input,EVENT_FILTER_PARAM,FOLLOW_EVENT_FILTER_PARAM_in_filterParam5925); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_filterParam5927);
            valueExpr();

            state._fsp--;

            // EsperEPL2Ast.g:852:35: ( valueExpr )*
            loop305:
            do {
                int alt305=2;
                int LA305_0 = input.LA(1);

                if ( ((LA305_0>=IN_SET && LA305_0<=REGEXP)||LA305_0==NOT_EXPR||(LA305_0>=SUM && LA305_0<=AVG)||(LA305_0>=COALESCE && LA305_0<=COUNT)||(LA305_0>=CASE && LA305_0<=CASE2)||LA305_0==ISTREAM||(LA305_0>=PREVIOUS && LA305_0<=EXISTS)||(LA305_0>=INSTANCEOF && LA305_0<=CURRENT_TIMESTAMP)||LA305_0==NEWKW||(LA305_0>=EVAL_AND_EXPR && LA305_0<=EVAL_NOTEQUALS_GROUP_EXPR)||LA305_0==EVENT_PROP_EXPR||LA305_0==CONCAT||(LA305_0>=LIB_FUNC_CHAIN && LA305_0<=DOT_EXPR)||LA305_0==ARRAY_EXPR||(LA305_0>=NOT_IN_SET && LA305_0<=NOT_REGEXP)||(LA305_0>=IN_RANGE && LA305_0<=SUBSELECT_EXPR)||(LA305_0>=EXISTS_SUBSELECT_EXPR && LA305_0<=NOT_IN_SUBSELECT_EXPR)||LA305_0==SUBSTITUTION||(LA305_0>=FIRST_AGGREG && LA305_0<=WINDOW_AGGREG)||(LA305_0>=INT_TYPE && LA305_0<=NULL_TYPE)||(LA305_0>=JSON_OBJECT && LA305_0<=JSON_ARRAY)||LA305_0==STAR||(LA305_0>=LT && LA305_0<=GT)||(LA305_0>=BOR && LA305_0<=PLUS)||(LA305_0>=BAND && LA305_0<=BXOR)||(LA305_0>=LE && LA305_0<=GE)||(LA305_0>=MINUS && LA305_0<=MOD)||(LA305_0>=EVAL_IS_GROUP_EXPR && LA305_0<=EVAL_ISNOT_GROUP_EXPR)) ) {
                    alt305=1;
                }


                switch (alt305) {
            	case 1 :
            	    // EsperEPL2Ast.g:852:36: valueExpr
            	    {
            	    pushFollow(FOLLOW_valueExpr_in_filterParam5930);
            	    valueExpr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop305;
                }
            } while (true);


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "filterParam"


    // $ANTLR start "filterParamComparator"
    // EsperEPL2Ast.g:855:1: filterParamComparator : ( ^( EQUALS filterAtom ) | ^( NOT_EQUAL filterAtom ) | ^( LT filterAtom ) | ^( LE filterAtom ) | ^( GT filterAtom ) | ^( GE filterAtom ) | ^( EVENT_FILTER_RANGE ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_NOT_RANGE ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_IN ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier )* ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_NOT_IN ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier )* ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_BETWEEN ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ) | ^( EVENT_FILTER_NOT_BETWEEN ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ) );
    public final void filterParamComparator() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:856:2: ( ^( EQUALS filterAtom ) | ^( NOT_EQUAL filterAtom ) | ^( LT filterAtom ) | ^( LE filterAtom ) | ^( GT filterAtom ) | ^( GE filterAtom ) | ^( EVENT_FILTER_RANGE ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_NOT_RANGE ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_IN ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier )* ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_NOT_IN ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier )* ( RPAREN | RBRACK ) ) | ^( EVENT_FILTER_BETWEEN ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ) | ^( EVENT_FILTER_NOT_BETWEEN ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ) )
            int alt318=12;
            switch ( input.LA(1) ) {
            case EQUALS:
                {
                alt318=1;
                }
                break;
            case NOT_EQUAL:
                {
                alt318=2;
                }
                break;
            case LT:
                {
                alt318=3;
                }
                break;
            case LE:
                {
                alt318=4;
                }
                break;
            case GT:
                {
                alt318=5;
                }
                break;
            case GE:
                {
                alt318=6;
                }
                break;
            case EVENT_FILTER_RANGE:
                {
                alt318=7;
                }
                break;
            case EVENT_FILTER_NOT_RANGE:
                {
                alt318=8;
                }
                break;
            case EVENT_FILTER_IN:
                {
                alt318=9;
                }
                break;
            case EVENT_FILTER_NOT_IN:
                {
                alt318=10;
                }
                break;
            case EVENT_FILTER_BETWEEN:
                {
                alt318=11;
                }
                break;
            case EVENT_FILTER_NOT_BETWEEN:
                {
                alt318=12;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 318, 0, input);

                throw nvae;
            }

            switch (alt318) {
                case 1 :
                    // EsperEPL2Ast.g:856:4: ^( EQUALS filterAtom )
                    {
                    match(input,EQUALS,FOLLOW_EQUALS_in_filterParamComparator5946); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_filterAtom_in_filterParamComparator5948);
                    filterAtom();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:857:4: ^( NOT_EQUAL filterAtom )
                    {
                    match(input,NOT_EQUAL,FOLLOW_NOT_EQUAL_in_filterParamComparator5955); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_filterAtom_in_filterParamComparator5957);
                    filterAtom();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:858:4: ^( LT filterAtom )
                    {
                    match(input,LT,FOLLOW_LT_in_filterParamComparator5964); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_filterAtom_in_filterParamComparator5966);
                    filterAtom();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:859:4: ^( LE filterAtom )
                    {
                    match(input,LE,FOLLOW_LE_in_filterParamComparator5973); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_filterAtom_in_filterParamComparator5975);
                    filterAtom();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:860:4: ^( GT filterAtom )
                    {
                    match(input,GT,FOLLOW_GT_in_filterParamComparator5982); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_filterAtom_in_filterParamComparator5984);
                    filterAtom();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:861:4: ^( GE filterAtom )
                    {
                    match(input,GE,FOLLOW_GE_in_filterParamComparator5991); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_filterAtom_in_filterParamComparator5993);
                    filterAtom();

                    state._fsp--;


                    match(input, Token.UP, null); 

                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:862:4: ^( EVENT_FILTER_RANGE ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ( RPAREN | RBRACK ) )
                    {
                    match(input,EVENT_FILTER_RANGE,FOLLOW_EVENT_FILTER_RANGE_in_filterParamComparator6000); 

                    match(input, Token.DOWN, null); 
                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // EsperEPL2Ast.g:862:41: ( constant[false] | filterIdentifier )
                    int alt306=2;
                    int LA306_0 = input.LA(1);

                    if ( ((LA306_0>=INT_TYPE && LA306_0<=NULL_TYPE)) ) {
                        alt306=1;
                    }
                    else if ( (LA306_0==EVENT_FILTER_IDENT) ) {
                        alt306=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 306, 0, input);

                        throw nvae;
                    }
                    switch (alt306) {
                        case 1 :
                            // EsperEPL2Ast.g:862:42: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6009);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:862:58: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6012);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:862:76: ( constant[false] | filterIdentifier )
                    int alt307=2;
                    int LA307_0 = input.LA(1);

                    if ( ((LA307_0>=INT_TYPE && LA307_0<=NULL_TYPE)) ) {
                        alt307=1;
                    }
                    else if ( (LA307_0==EVENT_FILTER_IDENT) ) {
                        alt307=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 307, 0, input);

                        throw nvae;
                    }
                    switch (alt307) {
                        case 1 :
                            // EsperEPL2Ast.g:862:77: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6016);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:862:93: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6019);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 8 :
                    // EsperEPL2Ast.g:863:4: ^( EVENT_FILTER_NOT_RANGE ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) ( RPAREN | RBRACK ) )
                    {
                    match(input,EVENT_FILTER_NOT_RANGE,FOLLOW_EVENT_FILTER_NOT_RANGE_in_filterParamComparator6033); 

                    match(input, Token.DOWN, null); 
                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // EsperEPL2Ast.g:863:45: ( constant[false] | filterIdentifier )
                    int alt308=2;
                    int LA308_0 = input.LA(1);

                    if ( ((LA308_0>=INT_TYPE && LA308_0<=NULL_TYPE)) ) {
                        alt308=1;
                    }
                    else if ( (LA308_0==EVENT_FILTER_IDENT) ) {
                        alt308=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 308, 0, input);

                        throw nvae;
                    }
                    switch (alt308) {
                        case 1 :
                            // EsperEPL2Ast.g:863:46: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6042);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:863:62: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6045);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:863:80: ( constant[false] | filterIdentifier )
                    int alt309=2;
                    int LA309_0 = input.LA(1);

                    if ( ((LA309_0>=INT_TYPE && LA309_0<=NULL_TYPE)) ) {
                        alt309=1;
                    }
                    else if ( (LA309_0==EVENT_FILTER_IDENT) ) {
                        alt309=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 309, 0, input);

                        throw nvae;
                    }
                    switch (alt309) {
                        case 1 :
                            // EsperEPL2Ast.g:863:81: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6049);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:863:97: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6052);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 9 :
                    // EsperEPL2Ast.g:864:4: ^( EVENT_FILTER_IN ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier )* ( RPAREN | RBRACK ) )
                    {
                    match(input,EVENT_FILTER_IN,FOLLOW_EVENT_FILTER_IN_in_filterParamComparator6066); 

                    match(input, Token.DOWN, null); 
                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // EsperEPL2Ast.g:864:38: ( constant[false] | filterIdentifier )
                    int alt310=2;
                    int LA310_0 = input.LA(1);

                    if ( ((LA310_0>=INT_TYPE && LA310_0<=NULL_TYPE)) ) {
                        alt310=1;
                    }
                    else if ( (LA310_0==EVENT_FILTER_IDENT) ) {
                        alt310=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 310, 0, input);

                        throw nvae;
                    }
                    switch (alt310) {
                        case 1 :
                            // EsperEPL2Ast.g:864:39: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6075);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:864:55: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6078);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:864:73: ( constant[false] | filterIdentifier )*
                    loop311:
                    do {
                        int alt311=3;
                        int LA311_0 = input.LA(1);

                        if ( ((LA311_0>=INT_TYPE && LA311_0<=NULL_TYPE)) ) {
                            alt311=1;
                        }
                        else if ( (LA311_0==EVENT_FILTER_IDENT) ) {
                            alt311=2;
                        }


                        switch (alt311) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:864:74: constant[false]
                    	    {
                    	    pushFollow(FOLLOW_constant_in_filterParamComparator6082);
                    	    constant(false);

                    	    state._fsp--;


                    	    }
                    	    break;
                    	case 2 :
                    	    // EsperEPL2Ast.g:864:90: filterIdentifier
                    	    {
                    	    pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6085);
                    	    filterIdentifier();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop311;
                        }
                    } while (true);

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 10 :
                    // EsperEPL2Ast.g:865:4: ^( EVENT_FILTER_NOT_IN ( LPAREN | LBRACK ) ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier )* ( RPAREN | RBRACK ) )
                    {
                    match(input,EVENT_FILTER_NOT_IN,FOLLOW_EVENT_FILTER_NOT_IN_in_filterParamComparator6100); 

                    match(input, Token.DOWN, null); 
                    if ( input.LA(1)==LBRACK||input.LA(1)==LPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    // EsperEPL2Ast.g:865:42: ( constant[false] | filterIdentifier )
                    int alt312=2;
                    int LA312_0 = input.LA(1);

                    if ( ((LA312_0>=INT_TYPE && LA312_0<=NULL_TYPE)) ) {
                        alt312=1;
                    }
                    else if ( (LA312_0==EVENT_FILTER_IDENT) ) {
                        alt312=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 312, 0, input);

                        throw nvae;
                    }
                    switch (alt312) {
                        case 1 :
                            // EsperEPL2Ast.g:865:43: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6109);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:865:59: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6112);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:865:77: ( constant[false] | filterIdentifier )*
                    loop313:
                    do {
                        int alt313=3;
                        int LA313_0 = input.LA(1);

                        if ( ((LA313_0>=INT_TYPE && LA313_0<=NULL_TYPE)) ) {
                            alt313=1;
                        }
                        else if ( (LA313_0==EVENT_FILTER_IDENT) ) {
                            alt313=2;
                        }


                        switch (alt313) {
                    	case 1 :
                    	    // EsperEPL2Ast.g:865:78: constant[false]
                    	    {
                    	    pushFollow(FOLLOW_constant_in_filterParamComparator6116);
                    	    constant(false);

                    	    state._fsp--;


                    	    }
                    	    break;
                    	case 2 :
                    	    // EsperEPL2Ast.g:865:94: filterIdentifier
                    	    {
                    	    pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6119);
                    	    filterIdentifier();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop313;
                        }
                    } while (true);

                    if ( input.LA(1)==RBRACK||input.LA(1)==RPAREN ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 11 :
                    // EsperEPL2Ast.g:866:4: ^( EVENT_FILTER_BETWEEN ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) )
                    {
                    match(input,EVENT_FILTER_BETWEEN,FOLLOW_EVENT_FILTER_BETWEEN_in_filterParamComparator6134); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:866:27: ( constant[false] | filterIdentifier )
                    int alt314=2;
                    int LA314_0 = input.LA(1);

                    if ( ((LA314_0>=INT_TYPE && LA314_0<=NULL_TYPE)) ) {
                        alt314=1;
                    }
                    else if ( (LA314_0==EVENT_FILTER_IDENT) ) {
                        alt314=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 314, 0, input);

                        throw nvae;
                    }
                    switch (alt314) {
                        case 1 :
                            // EsperEPL2Ast.g:866:28: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6137);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:866:44: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6140);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:866:62: ( constant[false] | filterIdentifier )
                    int alt315=2;
                    int LA315_0 = input.LA(1);

                    if ( ((LA315_0>=INT_TYPE && LA315_0<=NULL_TYPE)) ) {
                        alt315=1;
                    }
                    else if ( (LA315_0==EVENT_FILTER_IDENT) ) {
                        alt315=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 315, 0, input);

                        throw nvae;
                    }
                    switch (alt315) {
                        case 1 :
                            // EsperEPL2Ast.g:866:63: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6144);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:866:79: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6147);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 12 :
                    // EsperEPL2Ast.g:867:4: ^( EVENT_FILTER_NOT_BETWEEN ( constant[false] | filterIdentifier ) ( constant[false] | filterIdentifier ) )
                    {
                    match(input,EVENT_FILTER_NOT_BETWEEN,FOLLOW_EVENT_FILTER_NOT_BETWEEN_in_filterParamComparator6155); 

                    match(input, Token.DOWN, null); 
                    // EsperEPL2Ast.g:867:31: ( constant[false] | filterIdentifier )
                    int alt316=2;
                    int LA316_0 = input.LA(1);

                    if ( ((LA316_0>=INT_TYPE && LA316_0<=NULL_TYPE)) ) {
                        alt316=1;
                    }
                    else if ( (LA316_0==EVENT_FILTER_IDENT) ) {
                        alt316=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 316, 0, input);

                        throw nvae;
                    }
                    switch (alt316) {
                        case 1 :
                            // EsperEPL2Ast.g:867:32: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6158);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:867:48: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6161);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:867:66: ( constant[false] | filterIdentifier )
                    int alt317=2;
                    int LA317_0 = input.LA(1);

                    if ( ((LA317_0>=INT_TYPE && LA317_0<=NULL_TYPE)) ) {
                        alt317=1;
                    }
                    else if ( (LA317_0==EVENT_FILTER_IDENT) ) {
                        alt317=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 317, 0, input);

                        throw nvae;
                    }
                    switch (alt317) {
                        case 1 :
                            // EsperEPL2Ast.g:867:67: constant[false]
                            {
                            pushFollow(FOLLOW_constant_in_filterParamComparator6165);
                            constant(false);

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // EsperEPL2Ast.g:867:83: filterIdentifier
                            {
                            pushFollow(FOLLOW_filterIdentifier_in_filterParamComparator6168);
                            filterIdentifier();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "filterParamComparator"


    // $ANTLR start "filterAtom"
    // EsperEPL2Ast.g:870:1: filterAtom : ( constant[false] | filterIdentifier );
    public final void filterAtom() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:871:2: ( constant[false] | filterIdentifier )
            int alt319=2;
            int LA319_0 = input.LA(1);

            if ( ((LA319_0>=INT_TYPE && LA319_0<=NULL_TYPE)) ) {
                alt319=1;
            }
            else if ( (LA319_0==EVENT_FILTER_IDENT) ) {
                alt319=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 319, 0, input);

                throw nvae;
            }
            switch (alt319) {
                case 1 :
                    // EsperEPL2Ast.g:871:4: constant[false]
                    {
                    pushFollow(FOLLOW_constant_in_filterAtom6182);
                    constant(false);

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:872:4: filterIdentifier
                    {
                    pushFollow(FOLLOW_filterIdentifier_in_filterAtom6188);
                    filterIdentifier();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "filterAtom"


    // $ANTLR start "filterIdentifier"
    // EsperEPL2Ast.g:874:1: filterIdentifier : ^( EVENT_FILTER_IDENT IDENT eventPropertyExpr[true] ) ;
    public final void filterIdentifier() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:875:2: ( ^( EVENT_FILTER_IDENT IDENT eventPropertyExpr[true] ) )
            // EsperEPL2Ast.g:875:4: ^( EVENT_FILTER_IDENT IDENT eventPropertyExpr[true] )
            {
            match(input,EVENT_FILTER_IDENT,FOLLOW_EVENT_FILTER_IDENT_in_filterIdentifier6199); 

            match(input, Token.DOWN, null); 
            match(input,IDENT,FOLLOW_IDENT_in_filterIdentifier6201); 
            pushFollow(FOLLOW_eventPropertyExpr_in_filterIdentifier6203);
            eventPropertyExpr(true);

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "filterIdentifier"


    // $ANTLR start "eventPropertyExpr"
    // EsperEPL2Ast.g:878:1: eventPropertyExpr[boolean isLeaveNode] : ^(p= EVENT_PROP_EXPR eventPropertyAtomic ( eventPropertyAtomic )* ) ;
    public final void eventPropertyExpr(boolean isLeaveNode) throws RecognitionException {
        CommonTree p=null;

        try {
            // EsperEPL2Ast.g:879:2: ( ^(p= EVENT_PROP_EXPR eventPropertyAtomic ( eventPropertyAtomic )* ) )
            // EsperEPL2Ast.g:879:4: ^(p= EVENT_PROP_EXPR eventPropertyAtomic ( eventPropertyAtomic )* )
            {
            p=(CommonTree)match(input,EVENT_PROP_EXPR,FOLLOW_EVENT_PROP_EXPR_in_eventPropertyExpr6222); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_eventPropertyAtomic_in_eventPropertyExpr6224);
            eventPropertyAtomic();

            state._fsp--;

            // EsperEPL2Ast.g:879:44: ( eventPropertyAtomic )*
            loop320:
            do {
                int alt320=2;
                int LA320_0 = input.LA(1);

                if ( ((LA320_0>=EVENT_PROP_SIMPLE && LA320_0<=EVENT_PROP_DYNAMIC_MAPPED)) ) {
                    alt320=1;
                }


                switch (alt320) {
            	case 1 :
            	    // EsperEPL2Ast.g:879:45: eventPropertyAtomic
            	    {
            	    pushFollow(FOLLOW_eventPropertyAtomic_in_eventPropertyExpr6227);
            	    eventPropertyAtomic();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop320;
                }
            } while (true);


            match(input, Token.UP, null); 
             if (isLeaveNode) leaveNode(p); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "eventPropertyExpr"


    // $ANTLR start "eventPropertyAtomic"
    // EsperEPL2Ast.g:882:1: eventPropertyAtomic : ( ^( EVENT_PROP_SIMPLE IDENT ) | ^( EVENT_PROP_INDEXED IDENT NUM_INT ) | ^( EVENT_PROP_MAPPED IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) ) | ^( EVENT_PROP_DYNAMIC_SIMPLE IDENT ) | ^( EVENT_PROP_DYNAMIC_INDEXED IDENT NUM_INT ) | ^( EVENT_PROP_DYNAMIC_MAPPED IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) ) );
    public final void eventPropertyAtomic() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:883:2: ( ^( EVENT_PROP_SIMPLE IDENT ) | ^( EVENT_PROP_INDEXED IDENT NUM_INT ) | ^( EVENT_PROP_MAPPED IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) ) | ^( EVENT_PROP_DYNAMIC_SIMPLE IDENT ) | ^( EVENT_PROP_DYNAMIC_INDEXED IDENT NUM_INT ) | ^( EVENT_PROP_DYNAMIC_MAPPED IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) ) )
            int alt321=6;
            switch ( input.LA(1) ) {
            case EVENT_PROP_SIMPLE:
                {
                alt321=1;
                }
                break;
            case EVENT_PROP_INDEXED:
                {
                alt321=2;
                }
                break;
            case EVENT_PROP_MAPPED:
                {
                alt321=3;
                }
                break;
            case EVENT_PROP_DYNAMIC_SIMPLE:
                {
                alt321=4;
                }
                break;
            case EVENT_PROP_DYNAMIC_INDEXED:
                {
                alt321=5;
                }
                break;
            case EVENT_PROP_DYNAMIC_MAPPED:
                {
                alt321=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 321, 0, input);

                throw nvae;
            }

            switch (alt321) {
                case 1 :
                    // EsperEPL2Ast.g:883:4: ^( EVENT_PROP_SIMPLE IDENT )
                    {
                    match(input,EVENT_PROP_SIMPLE,FOLLOW_EVENT_PROP_SIMPLE_in_eventPropertyAtomic6246); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_eventPropertyAtomic6248); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:884:4: ^( EVENT_PROP_INDEXED IDENT NUM_INT )
                    {
                    match(input,EVENT_PROP_INDEXED,FOLLOW_EVENT_PROP_INDEXED_in_eventPropertyAtomic6255); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_eventPropertyAtomic6257); 
                    match(input,NUM_INT,FOLLOW_NUM_INT_in_eventPropertyAtomic6259); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:885:4: ^( EVENT_PROP_MAPPED IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) )
                    {
                    match(input,EVENT_PROP_MAPPED,FOLLOW_EVENT_PROP_MAPPED_in_eventPropertyAtomic6266); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_eventPropertyAtomic6268); 
                    if ( (input.LA(1)>=STRING_LITERAL && input.LA(1)<=QUOTED_STRING_LITERAL) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:886:4: ^( EVENT_PROP_DYNAMIC_SIMPLE IDENT )
                    {
                    match(input,EVENT_PROP_DYNAMIC_SIMPLE,FOLLOW_EVENT_PROP_DYNAMIC_SIMPLE_in_eventPropertyAtomic6283); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_eventPropertyAtomic6285); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:887:4: ^( EVENT_PROP_DYNAMIC_INDEXED IDENT NUM_INT )
                    {
                    match(input,EVENT_PROP_DYNAMIC_INDEXED,FOLLOW_EVENT_PROP_DYNAMIC_INDEXED_in_eventPropertyAtomic6292); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_eventPropertyAtomic6294); 
                    match(input,NUM_INT,FOLLOW_NUM_INT_in_eventPropertyAtomic6296); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:888:4: ^( EVENT_PROP_DYNAMIC_MAPPED IDENT ( STRING_LITERAL | QUOTED_STRING_LITERAL ) )
                    {
                    match(input,EVENT_PROP_DYNAMIC_MAPPED,FOLLOW_EVENT_PROP_DYNAMIC_MAPPED_in_eventPropertyAtomic6303); 

                    match(input, Token.DOWN, null); 
                    match(input,IDENT,FOLLOW_IDENT_in_eventPropertyAtomic6305); 
                    if ( (input.LA(1)>=STRING_LITERAL && input.LA(1)<=QUOTED_STRING_LITERAL) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "eventPropertyAtomic"


    // $ANTLR start "timePeriod"
    // EsperEPL2Ast.g:891:1: timePeriod : ^(t= TIME_PERIOD timePeriodDef ) ;
    public final void timePeriod() throws RecognitionException {
        CommonTree t=null;

        try {
            // EsperEPL2Ast.g:892:2: ( ^(t= TIME_PERIOD timePeriodDef ) )
            // EsperEPL2Ast.g:892:5: ^(t= TIME_PERIOD timePeriodDef )
            {
            t=(CommonTree)match(input,TIME_PERIOD,FOLLOW_TIME_PERIOD_in_timePeriod6332); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_timePeriodDef_in_timePeriod6334);
            timePeriodDef();

            state._fsp--;

             leaveNode(t); 

            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "timePeriod"


    // $ANTLR start "timePeriodDef"
    // EsperEPL2Ast.g:895:1: timePeriodDef : ( yearPart ( monthPart )? ( weekPart )? ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | monthPart ( weekPart )? ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | weekPart ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | dayPart ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | hourPart ( minutePart )? ( secondPart )? ( millisecondPart )? | minutePart ( secondPart )? ( millisecondPart )? | secondPart ( millisecondPart )? | millisecondPart );
    public final void timePeriodDef() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:896:2: ( yearPart ( monthPart )? ( weekPart )? ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | monthPart ( weekPart )? ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | weekPart ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | dayPart ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )? | hourPart ( minutePart )? ( secondPart )? ( millisecondPart )? | minutePart ( secondPart )? ( millisecondPart )? | secondPart ( millisecondPart )? | millisecondPart )
            int alt350=8;
            switch ( input.LA(1) ) {
            case YEAR_PART:
                {
                alt350=1;
                }
                break;
            case MONTH_PART:
                {
                alt350=2;
                }
                break;
            case WEEK_PART:
                {
                alt350=3;
                }
                break;
            case DAY_PART:
                {
                alt350=4;
                }
                break;
            case HOUR_PART:
                {
                alt350=5;
                }
                break;
            case MINUTE_PART:
                {
                alt350=6;
                }
                break;
            case SECOND_PART:
                {
                alt350=7;
                }
                break;
            case MILLISECOND_PART:
                {
                alt350=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 350, 0, input);

                throw nvae;
            }

            switch (alt350) {
                case 1 :
                    // EsperEPL2Ast.g:896:5: yearPart ( monthPart )? ( weekPart )? ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )?
                    {
                    pushFollow(FOLLOW_yearPart_in_timePeriodDef6350);
                    yearPart();

                    state._fsp--;

                    // EsperEPL2Ast.g:896:14: ( monthPart )?
                    int alt322=2;
                    int LA322_0 = input.LA(1);

                    if ( (LA322_0==MONTH_PART) ) {
                        alt322=1;
                    }
                    switch (alt322) {
                        case 1 :
                            // EsperEPL2Ast.g:896:15: monthPart
                            {
                            pushFollow(FOLLOW_monthPart_in_timePeriodDef6353);
                            monthPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:896:27: ( weekPart )?
                    int alt323=2;
                    int LA323_0 = input.LA(1);

                    if ( (LA323_0==WEEK_PART) ) {
                        alt323=1;
                    }
                    switch (alt323) {
                        case 1 :
                            // EsperEPL2Ast.g:896:28: weekPart
                            {
                            pushFollow(FOLLOW_weekPart_in_timePeriodDef6358);
                            weekPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:896:39: ( dayPart )?
                    int alt324=2;
                    int LA324_0 = input.LA(1);

                    if ( (LA324_0==DAY_PART) ) {
                        alt324=1;
                    }
                    switch (alt324) {
                        case 1 :
                            // EsperEPL2Ast.g:896:40: dayPart
                            {
                            pushFollow(FOLLOW_dayPart_in_timePeriodDef6363);
                            dayPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:896:50: ( hourPart )?
                    int alt325=2;
                    int LA325_0 = input.LA(1);

                    if ( (LA325_0==HOUR_PART) ) {
                        alt325=1;
                    }
                    switch (alt325) {
                        case 1 :
                            // EsperEPL2Ast.g:896:51: hourPart
                            {
                            pushFollow(FOLLOW_hourPart_in_timePeriodDef6368);
                            hourPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:896:62: ( minutePart )?
                    int alt326=2;
                    int LA326_0 = input.LA(1);

                    if ( (LA326_0==MINUTE_PART) ) {
                        alt326=1;
                    }
                    switch (alt326) {
                        case 1 :
                            // EsperEPL2Ast.g:896:63: minutePart
                            {
                            pushFollow(FOLLOW_minutePart_in_timePeriodDef6373);
                            minutePart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:896:76: ( secondPart )?
                    int alt327=2;
                    int LA327_0 = input.LA(1);

                    if ( (LA327_0==SECOND_PART) ) {
                        alt327=1;
                    }
                    switch (alt327) {
                        case 1 :
                            // EsperEPL2Ast.g:896:77: secondPart
                            {
                            pushFollow(FOLLOW_secondPart_in_timePeriodDef6378);
                            secondPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:896:90: ( millisecondPart )?
                    int alt328=2;
                    int LA328_0 = input.LA(1);

                    if ( (LA328_0==MILLISECOND_PART) ) {
                        alt328=1;
                    }
                    switch (alt328) {
                        case 1 :
                            // EsperEPL2Ast.g:896:91: millisecondPart
                            {
                            pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6383);
                            millisecondPart();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:897:5: monthPart ( weekPart )? ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )?
                    {
                    pushFollow(FOLLOW_monthPart_in_timePeriodDef6391);
                    monthPart();

                    state._fsp--;

                    // EsperEPL2Ast.g:897:15: ( weekPart )?
                    int alt329=2;
                    int LA329_0 = input.LA(1);

                    if ( (LA329_0==WEEK_PART) ) {
                        alt329=1;
                    }
                    switch (alt329) {
                        case 1 :
                            // EsperEPL2Ast.g:897:16: weekPart
                            {
                            pushFollow(FOLLOW_weekPart_in_timePeriodDef6394);
                            weekPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:897:27: ( dayPart )?
                    int alt330=2;
                    int LA330_0 = input.LA(1);

                    if ( (LA330_0==DAY_PART) ) {
                        alt330=1;
                    }
                    switch (alt330) {
                        case 1 :
                            // EsperEPL2Ast.g:897:28: dayPart
                            {
                            pushFollow(FOLLOW_dayPart_in_timePeriodDef6399);
                            dayPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:897:38: ( hourPart )?
                    int alt331=2;
                    int LA331_0 = input.LA(1);

                    if ( (LA331_0==HOUR_PART) ) {
                        alt331=1;
                    }
                    switch (alt331) {
                        case 1 :
                            // EsperEPL2Ast.g:897:39: hourPart
                            {
                            pushFollow(FOLLOW_hourPart_in_timePeriodDef6404);
                            hourPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:897:50: ( minutePart )?
                    int alt332=2;
                    int LA332_0 = input.LA(1);

                    if ( (LA332_0==MINUTE_PART) ) {
                        alt332=1;
                    }
                    switch (alt332) {
                        case 1 :
                            // EsperEPL2Ast.g:897:51: minutePart
                            {
                            pushFollow(FOLLOW_minutePart_in_timePeriodDef6409);
                            minutePart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:897:64: ( secondPart )?
                    int alt333=2;
                    int LA333_0 = input.LA(1);

                    if ( (LA333_0==SECOND_PART) ) {
                        alt333=1;
                    }
                    switch (alt333) {
                        case 1 :
                            // EsperEPL2Ast.g:897:65: secondPart
                            {
                            pushFollow(FOLLOW_secondPart_in_timePeriodDef6414);
                            secondPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:897:78: ( millisecondPart )?
                    int alt334=2;
                    int LA334_0 = input.LA(1);

                    if ( (LA334_0==MILLISECOND_PART) ) {
                        alt334=1;
                    }
                    switch (alt334) {
                        case 1 :
                            // EsperEPL2Ast.g:897:79: millisecondPart
                            {
                            pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6419);
                            millisecondPart();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:898:5: weekPart ( dayPart )? ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )?
                    {
                    pushFollow(FOLLOW_weekPart_in_timePeriodDef6427);
                    weekPart();

                    state._fsp--;

                    // EsperEPL2Ast.g:898:14: ( dayPart )?
                    int alt335=2;
                    int LA335_0 = input.LA(1);

                    if ( (LA335_0==DAY_PART) ) {
                        alt335=1;
                    }
                    switch (alt335) {
                        case 1 :
                            // EsperEPL2Ast.g:898:15: dayPart
                            {
                            pushFollow(FOLLOW_dayPart_in_timePeriodDef6430);
                            dayPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:898:25: ( hourPart )?
                    int alt336=2;
                    int LA336_0 = input.LA(1);

                    if ( (LA336_0==HOUR_PART) ) {
                        alt336=1;
                    }
                    switch (alt336) {
                        case 1 :
                            // EsperEPL2Ast.g:898:26: hourPart
                            {
                            pushFollow(FOLLOW_hourPart_in_timePeriodDef6435);
                            hourPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:898:37: ( minutePart )?
                    int alt337=2;
                    int LA337_0 = input.LA(1);

                    if ( (LA337_0==MINUTE_PART) ) {
                        alt337=1;
                    }
                    switch (alt337) {
                        case 1 :
                            // EsperEPL2Ast.g:898:38: minutePart
                            {
                            pushFollow(FOLLOW_minutePart_in_timePeriodDef6440);
                            minutePart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:898:51: ( secondPart )?
                    int alt338=2;
                    int LA338_0 = input.LA(1);

                    if ( (LA338_0==SECOND_PART) ) {
                        alt338=1;
                    }
                    switch (alt338) {
                        case 1 :
                            // EsperEPL2Ast.g:898:52: secondPart
                            {
                            pushFollow(FOLLOW_secondPart_in_timePeriodDef6445);
                            secondPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:898:65: ( millisecondPart )?
                    int alt339=2;
                    int LA339_0 = input.LA(1);

                    if ( (LA339_0==MILLISECOND_PART) ) {
                        alt339=1;
                    }
                    switch (alt339) {
                        case 1 :
                            // EsperEPL2Ast.g:898:66: millisecondPart
                            {
                            pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6450);
                            millisecondPart();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:899:5: dayPart ( hourPart )? ( minutePart )? ( secondPart )? ( millisecondPart )?
                    {
                    pushFollow(FOLLOW_dayPart_in_timePeriodDef6458);
                    dayPart();

                    state._fsp--;

                    // EsperEPL2Ast.g:899:13: ( hourPart )?
                    int alt340=2;
                    int LA340_0 = input.LA(1);

                    if ( (LA340_0==HOUR_PART) ) {
                        alt340=1;
                    }
                    switch (alt340) {
                        case 1 :
                            // EsperEPL2Ast.g:899:14: hourPart
                            {
                            pushFollow(FOLLOW_hourPart_in_timePeriodDef6461);
                            hourPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:899:25: ( minutePart )?
                    int alt341=2;
                    int LA341_0 = input.LA(1);

                    if ( (LA341_0==MINUTE_PART) ) {
                        alt341=1;
                    }
                    switch (alt341) {
                        case 1 :
                            // EsperEPL2Ast.g:899:26: minutePart
                            {
                            pushFollow(FOLLOW_minutePart_in_timePeriodDef6466);
                            minutePart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:899:39: ( secondPart )?
                    int alt342=2;
                    int LA342_0 = input.LA(1);

                    if ( (LA342_0==SECOND_PART) ) {
                        alt342=1;
                    }
                    switch (alt342) {
                        case 1 :
                            // EsperEPL2Ast.g:899:40: secondPart
                            {
                            pushFollow(FOLLOW_secondPart_in_timePeriodDef6471);
                            secondPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:899:53: ( millisecondPart )?
                    int alt343=2;
                    int LA343_0 = input.LA(1);

                    if ( (LA343_0==MILLISECOND_PART) ) {
                        alt343=1;
                    }
                    switch (alt343) {
                        case 1 :
                            // EsperEPL2Ast.g:899:54: millisecondPart
                            {
                            pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6476);
                            millisecondPart();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:900:4: hourPart ( minutePart )? ( secondPart )? ( millisecondPart )?
                    {
                    pushFollow(FOLLOW_hourPart_in_timePeriodDef6483);
                    hourPart();

                    state._fsp--;

                    // EsperEPL2Ast.g:900:13: ( minutePart )?
                    int alt344=2;
                    int LA344_0 = input.LA(1);

                    if ( (LA344_0==MINUTE_PART) ) {
                        alt344=1;
                    }
                    switch (alt344) {
                        case 1 :
                            // EsperEPL2Ast.g:900:14: minutePart
                            {
                            pushFollow(FOLLOW_minutePart_in_timePeriodDef6486);
                            minutePart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:900:27: ( secondPart )?
                    int alt345=2;
                    int LA345_0 = input.LA(1);

                    if ( (LA345_0==SECOND_PART) ) {
                        alt345=1;
                    }
                    switch (alt345) {
                        case 1 :
                            // EsperEPL2Ast.g:900:28: secondPart
                            {
                            pushFollow(FOLLOW_secondPart_in_timePeriodDef6491);
                            secondPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:900:41: ( millisecondPart )?
                    int alt346=2;
                    int LA346_0 = input.LA(1);

                    if ( (LA346_0==MILLISECOND_PART) ) {
                        alt346=1;
                    }
                    switch (alt346) {
                        case 1 :
                            // EsperEPL2Ast.g:900:42: millisecondPart
                            {
                            pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6496);
                            millisecondPart();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:901:4: minutePart ( secondPart )? ( millisecondPart )?
                    {
                    pushFollow(FOLLOW_minutePart_in_timePeriodDef6503);
                    minutePart();

                    state._fsp--;

                    // EsperEPL2Ast.g:901:15: ( secondPart )?
                    int alt347=2;
                    int LA347_0 = input.LA(1);

                    if ( (LA347_0==SECOND_PART) ) {
                        alt347=1;
                    }
                    switch (alt347) {
                        case 1 :
                            // EsperEPL2Ast.g:901:16: secondPart
                            {
                            pushFollow(FOLLOW_secondPart_in_timePeriodDef6506);
                            secondPart();

                            state._fsp--;


                            }
                            break;

                    }

                    // EsperEPL2Ast.g:901:29: ( millisecondPart )?
                    int alt348=2;
                    int LA348_0 = input.LA(1);

                    if ( (LA348_0==MILLISECOND_PART) ) {
                        alt348=1;
                    }
                    switch (alt348) {
                        case 1 :
                            // EsperEPL2Ast.g:901:30: millisecondPart
                            {
                            pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6511);
                            millisecondPart();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:902:4: secondPart ( millisecondPart )?
                    {
                    pushFollow(FOLLOW_secondPart_in_timePeriodDef6518);
                    secondPart();

                    state._fsp--;

                    // EsperEPL2Ast.g:902:15: ( millisecondPart )?
                    int alt349=2;
                    int LA349_0 = input.LA(1);

                    if ( (LA349_0==MILLISECOND_PART) ) {
                        alt349=1;
                    }
                    switch (alt349) {
                        case 1 :
                            // EsperEPL2Ast.g:902:16: millisecondPart
                            {
                            pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6521);
                            millisecondPart();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 8 :
                    // EsperEPL2Ast.g:903:4: millisecondPart
                    {
                    pushFollow(FOLLOW_millisecondPart_in_timePeriodDef6528);
                    millisecondPart();

                    state._fsp--;


                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "timePeriodDef"


    // $ANTLR start "yearPart"
    // EsperEPL2Ast.g:906:1: yearPart : ^( YEAR_PART valueExpr ) ;
    public final void yearPart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:907:2: ( ^( YEAR_PART valueExpr ) )
            // EsperEPL2Ast.g:907:4: ^( YEAR_PART valueExpr )
            {
            match(input,YEAR_PART,FOLLOW_YEAR_PART_in_yearPart6542); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_yearPart6544);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "yearPart"


    // $ANTLR start "monthPart"
    // EsperEPL2Ast.g:910:1: monthPart : ^( MONTH_PART valueExpr ) ;
    public final void monthPart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:911:2: ( ^( MONTH_PART valueExpr ) )
            // EsperEPL2Ast.g:911:4: ^( MONTH_PART valueExpr )
            {
            match(input,MONTH_PART,FOLLOW_MONTH_PART_in_monthPart6559); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_monthPart6561);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "monthPart"


    // $ANTLR start "weekPart"
    // EsperEPL2Ast.g:914:1: weekPart : ^( WEEK_PART valueExpr ) ;
    public final void weekPart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:915:2: ( ^( WEEK_PART valueExpr ) )
            // EsperEPL2Ast.g:915:4: ^( WEEK_PART valueExpr )
            {
            match(input,WEEK_PART,FOLLOW_WEEK_PART_in_weekPart6576); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_weekPart6578);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "weekPart"


    // $ANTLR start "dayPart"
    // EsperEPL2Ast.g:918:1: dayPart : ^( DAY_PART valueExpr ) ;
    public final void dayPart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:919:2: ( ^( DAY_PART valueExpr ) )
            // EsperEPL2Ast.g:919:4: ^( DAY_PART valueExpr )
            {
            match(input,DAY_PART,FOLLOW_DAY_PART_in_dayPart6593); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_dayPart6595);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "dayPart"


    // $ANTLR start "hourPart"
    // EsperEPL2Ast.g:922:1: hourPart : ^( HOUR_PART valueExpr ) ;
    public final void hourPart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:923:2: ( ^( HOUR_PART valueExpr ) )
            // EsperEPL2Ast.g:923:4: ^( HOUR_PART valueExpr )
            {
            match(input,HOUR_PART,FOLLOW_HOUR_PART_in_hourPart6610); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_hourPart6612);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "hourPart"


    // $ANTLR start "minutePart"
    // EsperEPL2Ast.g:926:1: minutePart : ^( MINUTE_PART valueExpr ) ;
    public final void minutePart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:927:2: ( ^( MINUTE_PART valueExpr ) )
            // EsperEPL2Ast.g:927:4: ^( MINUTE_PART valueExpr )
            {
            match(input,MINUTE_PART,FOLLOW_MINUTE_PART_in_minutePart6627); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_minutePart6629);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "minutePart"


    // $ANTLR start "secondPart"
    // EsperEPL2Ast.g:930:1: secondPart : ^( SECOND_PART valueExpr ) ;
    public final void secondPart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:931:2: ( ^( SECOND_PART valueExpr ) )
            // EsperEPL2Ast.g:931:4: ^( SECOND_PART valueExpr )
            {
            match(input,SECOND_PART,FOLLOW_SECOND_PART_in_secondPart6644); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_secondPart6646);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "secondPart"


    // $ANTLR start "millisecondPart"
    // EsperEPL2Ast.g:934:1: millisecondPart : ^( MILLISECOND_PART valueExpr ) ;
    public final void millisecondPart() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:935:2: ( ^( MILLISECOND_PART valueExpr ) )
            // EsperEPL2Ast.g:935:4: ^( MILLISECOND_PART valueExpr )
            {
            match(input,MILLISECOND_PART,FOLLOW_MILLISECOND_PART_in_millisecondPart6661); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_valueExpr_in_millisecondPart6663);
            valueExpr();

            state._fsp--;


            match(input, Token.UP, null); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "millisecondPart"


    // $ANTLR start "substitution"
    // EsperEPL2Ast.g:938:1: substitution : s= SUBSTITUTION ;
    public final void substitution() throws RecognitionException {
        CommonTree s=null;

        try {
            // EsperEPL2Ast.g:939:2: (s= SUBSTITUTION )
            // EsperEPL2Ast.g:939:4: s= SUBSTITUTION
            {
            s=(CommonTree)match(input,SUBSTITUTION,FOLLOW_SUBSTITUTION_in_substitution6678); 
             leaveNode(s); 

            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "substitution"


    // $ANTLR start "constant"
    // EsperEPL2Ast.g:942:1: constant[boolean isLeaveNode] : (c= INT_TYPE | c= LONG_TYPE | c= FLOAT_TYPE | c= DOUBLE_TYPE | c= STRING_TYPE | c= BOOL_TYPE | c= NULL_TYPE );
    public final void constant(boolean isLeaveNode) throws RecognitionException {
        CommonTree c=null;

        try {
            // EsperEPL2Ast.g:943:2: (c= INT_TYPE | c= LONG_TYPE | c= FLOAT_TYPE | c= DOUBLE_TYPE | c= STRING_TYPE | c= BOOL_TYPE | c= NULL_TYPE )
            int alt351=7;
            switch ( input.LA(1) ) {
            case INT_TYPE:
                {
                alt351=1;
                }
                break;
            case LONG_TYPE:
                {
                alt351=2;
                }
                break;
            case FLOAT_TYPE:
                {
                alt351=3;
                }
                break;
            case DOUBLE_TYPE:
                {
                alt351=4;
                }
                break;
            case STRING_TYPE:
                {
                alt351=5;
                }
                break;
            case BOOL_TYPE:
                {
                alt351=6;
                }
                break;
            case NULL_TYPE:
                {
                alt351=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 351, 0, input);

                throw nvae;
            }

            switch (alt351) {
                case 1 :
                    // EsperEPL2Ast.g:943:4: c= INT_TYPE
                    {
                    c=(CommonTree)match(input,INT_TYPE,FOLLOW_INT_TYPE_in_constant6694); 
                     if (isLeaveNode) leaveNode(c); 

                    }
                    break;
                case 2 :
                    // EsperEPL2Ast.g:944:4: c= LONG_TYPE
                    {
                    c=(CommonTree)match(input,LONG_TYPE,FOLLOW_LONG_TYPE_in_constant6703); 
                     if (isLeaveNode) leaveNode(c); 

                    }
                    break;
                case 3 :
                    // EsperEPL2Ast.g:945:4: c= FLOAT_TYPE
                    {
                    c=(CommonTree)match(input,FLOAT_TYPE,FOLLOW_FLOAT_TYPE_in_constant6712); 
                     if (isLeaveNode) leaveNode(c); 

                    }
                    break;
                case 4 :
                    // EsperEPL2Ast.g:946:4: c= DOUBLE_TYPE
                    {
                    c=(CommonTree)match(input,DOUBLE_TYPE,FOLLOW_DOUBLE_TYPE_in_constant6721); 
                     if (isLeaveNode) leaveNode(c); 

                    }
                    break;
                case 5 :
                    // EsperEPL2Ast.g:947:11: c= STRING_TYPE
                    {
                    c=(CommonTree)match(input,STRING_TYPE,FOLLOW_STRING_TYPE_in_constant6737); 
                     if (isLeaveNode) leaveNode(c); 

                    }
                    break;
                case 6 :
                    // EsperEPL2Ast.g:948:11: c= BOOL_TYPE
                    {
                    c=(CommonTree)match(input,BOOL_TYPE,FOLLOW_BOOL_TYPE_in_constant6753); 
                     if (isLeaveNode) leaveNode(c); 

                    }
                    break;
                case 7 :
                    // EsperEPL2Ast.g:949:8: c= NULL_TYPE
                    {
                    c=(CommonTree)match(input,NULL_TYPE,FOLLOW_NULL_TYPE_in_constant6766); 
                     if (isLeaveNode) leaveNode(c); 

                    }
                    break;

            }
        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "constant"


    // $ANTLR start "number"
    // EsperEPL2Ast.g:952:1: number : ( INT_TYPE | LONG_TYPE | FLOAT_TYPE | DOUBLE_TYPE );
    public final void number() throws RecognitionException {
        try {
            // EsperEPL2Ast.g:953:2: ( INT_TYPE | LONG_TYPE | FLOAT_TYPE | DOUBLE_TYPE )
            // EsperEPL2Ast.g:
            {
            if ( (input.LA(1)>=INT_TYPE && input.LA(1)<=DOUBLE_TYPE) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }

          catch (RecognitionException rex) {
            throw rex;
          }
        finally {
        }
        return ;
    }
    // $ANTLR end "number"

    // Delegated rules


 

    public static final BitSet FOLLOW_ANNOTATION_in_annotation92 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_annotation94 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L,0x0000000000000000L,0x0007F00000000038L});
    public static final BitSet FOLLOW_elementValuePair_in_annotation96 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L,0x0000000000000000L,0x0007F00000000038L});
    public static final BitSet FOLLOW_elementValue_in_annotation99 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ANNOTATION_VALUE_in_elementValuePair117 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_elementValuePair119 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L,0x0000000000000000L,0x0007F00000000018L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair121 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_annotation_in_elementValue148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ANNOTATION_ARRAY_in_elementValue156 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_elementValue_in_elementValue158 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L,0x0000000000000000L,0x0007F00000000018L});
    public static final BitSet FOLLOW_constant_in_elementValue169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_elementValue179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXPRESSIONDECL_in_expressionDecl205 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_expressionDecl207 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_expressionDef_in_expressionDecl209 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L,0x0000000000000040L,0x0000000000000000L,0x0000000000000108L});
    public static final BitSet FOLLOW_exprCol_in_expressionDecl211 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000108L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_expressionDecl214 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000108L});
    public static final BitSet FOLLOW_COLON_in_expressionDecl219 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_expressionDecl221 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LBRACK_in_expressionDecl226 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GOES_in_expressionDef243 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_expressionDef245 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_expressionLambdaDecl_in_expressionDef247 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPRESSIONDECL_in_expressionDef255 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_expressionDef257 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GOES_in_expressionLambdaDecl271 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_expressionLambdaDecl274 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_exprCol_in_expressionLambdaDecl278 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EPL_EXPR_in_startEPLExpressionRule295 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotation_in_startEPLExpressionRule298 = new BitSet(new long[]{0x0000000000000000L,0x4800800000020000L,0x0000080000000000L,0x0200B00000000020L,0x0000000201801408L});
    public static final BitSet FOLLOW_expressionDecl_in_startEPLExpressionRule303 = new BitSet(new long[]{0x0000000000000000L,0x4800800000020000L,0x0000080000000000L,0x0200B00000000020L,0x0000000201801408L});
    public static final BitSet FOLLOW_eplExpressionRule_in_startEPLExpressionRule308 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_contextExpr_in_eplExpressionRule325 = new BitSet(new long[]{0x0000000000000000L,0x4000800000020000L,0x0000080000000000L,0x0200B00000000020L,0x0000000200001400L});
    public static final BitSet FOLLOW_selectExpr_in_eplExpressionRule329 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_createWindowExpr_in_eplExpressionRule333 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_createIndexExpr_in_eplExpressionRule337 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_createVariableExpr_in_eplExpressionRule341 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_createSchemaExpr_in_eplExpressionRule345 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_onExpr_in_eplExpressionRule350 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_updateExpr_in_eplExpressionRule354 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_createDataflow_in_eplExpressionRule358 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_fafDelete_in_eplExpressionRule362 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_fafUpdate_in_eplExpressionRule366 = new BitSet(new long[]{0x0000000000000002L,0x0040000000000000L});
    public static final BitSet FOLLOW_forExpr_in_eplExpressionRule369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createContextExpr_in_eplExpressionRule376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createExpr_in_eplExpressionRule380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTEXT_in_contextExpr395 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_contextExpr397 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_EXPR_in_onExpr416 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_onStreamExpr_in_onExpr418 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x011E000000000000L});
    public static final BitSet FOLLOW_onDeleteExpr_in_onExpr423 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_onUpdateExpr_in_onExpr427 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_onSelectExpr_in_onExpr431 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_onSelectInsertExpr_in_onExpr434 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0060000000000000L});
    public static final BitSet FOLLOW_onSelectInsertOutput_in_onExpr437 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_onSetExpr_in_onExpr444 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_onMergeExpr_in_onExpr448 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_STREAM_in_onStreamExpr470 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventFilterExpr_in_onStreamExpr473 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_patternInclusionExpression_in_onStreamExpr478 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_onStreamExpr481 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_MERGE_EXPR_in_onMergeExpr499 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_onMergeExpr501 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000030000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_onMergeExpr503 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000030000L,0x0000000000000020L});
    public static final BitSet FOLLOW_mergeItem_in_onMergeExpr506 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000030000L,0x0000000000000020L});
    public static final BitSet FOLLOW_whereClause_in_onMergeExpr509 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_mergeMatched_in_mergeItem525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mergeUnmatched_in_mergeItem529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MERGE_MAT_in_mergeMatched544 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mergeMatchedItem_in_mergeMatched546 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000001C01C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_mergeMatched549 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MERGE_UPD_in_mergeMatchedItem567 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_onSetAssignment_in_mergeMatchedItem569 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_whereClause_in_mergeMatchedItem572 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MERGE_DEL_in_mergeMatchedItem585 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_whereClause_in_mergeMatchedItem587 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_INT_TYPE_in_mergeMatchedItem591 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_mergeInsert_in_mergeMatchedItem599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MERGE_UNM_in_mergeUnmatched613 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_mergeInsert_in_mergeUnmatched615 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000001C01C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_mergeUnmatched618 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MERGE_INS_in_mergeInsert637 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_selectionList_in_mergeInsert639 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000082000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_mergeInsert641 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_exprCol_in_mergeInsert644 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_whereClause_in_mergeInsert647 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_UPDATE_EXPR_in_updateExpr667 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_updateDetails_in_updateExpr669 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_UPDATE_in_updateDetails686 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_updateDetails688 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_updateDetails690 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_onSetAssignment_in_updateDetails693 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_whereClause_in_updateDetails696 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_DELETE_EXPR_in_onDeleteExpr711 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_onExprFrom_in_onDeleteExpr713 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_whereClause_in_onDeleteExpr716 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_SELECT_EXPR_in_onSelectExpr736 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_insertIntoExpr_in_onSelectExpr738 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_DELETE_in_onSelectExpr741 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_DISTINCT_in_onSelectExpr744 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_selectionList_in_onSelectExpr747 = new BitSet(new long[]{0x0000000000000008L,0x0000200000000000L,0x0030000180000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_onExprFrom_in_onSelectExpr749 = new BitSet(new long[]{0x0000000000000008L,0x0000200000000000L,0x0030000180000000L});
    public static final BitSet FOLLOW_whereClause_in_onSelectExpr752 = new BitSet(new long[]{0x0000000000000008L,0x0000200000000000L,0x0030000100000000L});
    public static final BitSet FOLLOW_groupByClause_in_onSelectExpr756 = new BitSet(new long[]{0x0000000000000008L,0x0000200000000000L,0x0020000100000000L});
    public static final BitSet FOLLOW_havingClause_in_onSelectExpr759 = new BitSet(new long[]{0x0000000000000008L,0x0000200000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_orderByClause_in_onSelectExpr762 = new BitSet(new long[]{0x0000000000000008L,0x0000200000000000L});
    public static final BitSet FOLLOW_rowLimitClause_in_onSelectExpr765 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_SELECT_INSERT_EXPR_in_onSelectInsertExpr785 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_insertIntoExpr_in_onSelectInsertExpr787 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_selectionList_in_onSelectInsertExpr789 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_whereClause_in_onSelectInsertExpr791 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_SELECT_INSERT_OUTPUT_in_onSelectInsertOutput808 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_onSelectInsertOutput810 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_SET_EXPR_in_onSetExpr828 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_onSetAssignment_in_onSetExpr830 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_onSetAssignment_in_onSetExpr833 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_whereClause_in_onSetExpr837 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_UPDATE_EXPR_in_onUpdateExpr852 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_onExprFrom_in_onUpdateExpr854 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_onSetAssignment_in_onUpdateExpr856 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_whereClause_in_onUpdateExpr859 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_SET_EXPR_ITEM_in_onSetAssignment874 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_onSetAssignment876 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_onSetAssignment879 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ON_EXPR_FROM_in_onExprFrom893 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_onExprFrom895 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_onExprFrom898 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_WINDOW_EXPR_in_createWindowExpr916 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_createWindowExpr918 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000003L,0x0000000012000000L,0x0000400000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_viewListExpr_in_createWindowExpr921 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000003L,0x0000000012000000L,0x0000400000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_RETAINUNION_in_createWindowExpr925 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000003L,0x0000000012000000L,0x0000400000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_RETAININTERSECTION_in_createWindowExpr928 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000003L,0x0000000012000000L,0x0000400000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_createSelectionList_in_createWindowExpr942 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_createWindowExpr945 = new BitSet(new long[]{0x0040000000000008L});
    public static final BitSet FOLLOW_createColTypeList_in_createWindowExpr974 = new BitSet(new long[]{0x0040000000000008L});
    public static final BitSet FOLLOW_createWindowExprInsert_in_createWindowExpr985 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_INDEX_EXPR_in_createIndexExpr1005 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_createIndexExpr1007 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createIndexExpr1009 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_indexColList_in_createIndexExpr1011 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createIndexExpr1013 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INDEXCOL_in_indexColList1029 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_indexCol_in_indexColList1031 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_INDEXCOL_in_indexCol1046 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_indexCol1048 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_indexCol1050 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INSERT_in_createWindowExprInsert1064 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_createWindowExprInsert1066 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_WINDOW_SELECT_EXPR_in_createSelectionList1083 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createSelectionListElement_in_createSelectionList1085 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000100000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_createSelectionListElement_in_createSelectionList1088 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000100000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_CREATE_COL_TYPE_LIST_in_createColTypeList1107 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createColTypeListElement_in_createColTypeList1109 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_createColTypeListElement_in_createColTypeList1112 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_COL_TYPE_in_createColTypeListElement1127 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_createColTypeListElement1129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_createColTypeListElement1131 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_LBRACK_in_createColTypeListElement1133 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WILDCARD_SELECT_in_createSelectionListElement1148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECTION_ELEMENT_EXPR_in_createSelectionListElement1158 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_createSelectionListElement1178 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createSelectionListElement1182 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_constant_in_createSelectionListElement1204 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createSelectionListElement1207 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_VARIABLE_EXPR_in_createVariableExpr1243 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_createVariableExpr1245 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createVariableExpr1247 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D002BL,0x0000000000000018L});
    public static final BitSet FOLLOW_IDENT_in_createVariableExpr1249 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D000BL,0x0000000000000018L});
    public static final BitSet FOLLOW_LBRACK_in_createVariableExpr1252 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_createVariableExpr1255 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DELETE_in_fafDelete1274 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_fafDelete1276 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_fafDelete1278 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_whereClause_in_fafDelete1281 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_UPDATE_in_fafUpdate1303 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_updateDetails_in_fafUpdate1305 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_DATAFLOW_in_createDataflow1325 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_createDataflow1327 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000400001000L});
    public static final BitSet FOLLOW_gop_in_createDataflow1329 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000400001000L});
    public static final BitSet FOLLOW_GOP_in_gop1346 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_gop1348 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000012800000008L});
    public static final BitSet FOLLOW_gopParam_in_gop1354 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000012000000008L});
    public static final BitSet FOLLOW_gopOut_in_gop1357 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000010000000008L});
    public static final BitSet FOLLOW_gopDetail_in_gop1360 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_annotation_in_gop1364 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_createSchemaExpr_in_gop1373 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GOPPARAM_in_gopParam1388 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_gopParamItem_in_gopParam1390 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000001000000000L});
    public static final BitSet FOLLOW_GOPPARAMITM_in_gopParamItem1404 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_gopParamItem1406 = new BitSet(new long[]{0x0000000000020008L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_AS_in_gopParamItem1411 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_gopParamItem1413 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GOPOUT_in_gopOut1429 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_gopOutItem_in_gopOut1431 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_GOPOUTITM_in_gopOutItem1445 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_gopOutItem1447 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_gopOutTypeParam_in_gopOutItem1449 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_GOPOUTTYP_in_gopOutTypeParam1464 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_gopOutTypeParam1468 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_gopOutTypeParam_in_gopOutTypeParam1470 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_QUESTION_in_gopOutTypeParam1476 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GOPCFG_in_gopDetail1490 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_gopConfig_in_gopDetail1492 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000060000000000L,0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_GOPCFGITM_in_gopConfig1508 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_gopConfig1510 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_gopConfig1512 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GOPCFGEXP_in_gopConfig1523 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_gopConfig1525 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GOPCFGEPL_in_gopConfig1536 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_selectExpr_in_gopConfig1538 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_constant_in_jsonvalue1554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_jsonobject_in_jsonvalue1561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_jsonarray_in_jsonvalue1568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_JSON_OBJECT_in_jsonobject1585 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_jsonpair_in_jsonobject1587 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_JSON_ARRAY_in_jsonarray1616 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_jsonvalue_in_jsonarray1618 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_JSON_FIELD_in_jsonpair1636 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_jsonpair1639 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_IDENT_in_jsonpair1644 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_jsonvalue_in_jsonpair1647 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_in_createContextExpr1667 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_createContextExpr1669 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x00000000BE000000L});
    public static final BitSet FOLLOW_createContextDetail_in_createContextExpr1671 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_FIXED_in_createContextDetail1687 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createContextRangePoint_in_createContextDetail1689 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L,0x0000400000000000L,0x0000000000000004L,0x0000000100000000L});
    public static final BitSet FOLLOW_createContextRangePoint_in_createContextDetail1691 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_INIT_in_createContextDetail1698 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createContextRangePoint_in_createContextDetail1700 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L,0x0000400000000000L,0x0000000000000004L,0x0000000100000000L});
    public static final BitSet FOLLOW_createContextRangePoint_in_createContextDetail1702 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_PART_in_createContextDetail1709 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createContextPartitionItem_in_createContextDetail1711 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_CREATE_CTX_CAT_in_createContextDetail1719 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createContextCategoryItem_in_createContextDetail1721 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L,0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_eventFilterExpr_in_createContextDetail1724 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_COAL_in_createContextDetail1732 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createContextCoalesceItem_in_createContextDetail1734 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createContextDetail1737 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000F00000000000L});
    public static final BitSet FOLLOW_number_in_createContextDetail1739 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createContextDetail1741 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_NESTED_in_createContextDetail1749 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createContextNested_in_createContextDetail1751 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_createContextNested_in_createContextDetail1753 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_createContextFilter_in_createContextRangePoint1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_CTX_PATTERN_in_createContextRangePoint1774 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_patternInclusionExpression_in_createContextRangePoint1776 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createContextRangePoint1778 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_crontabLimitParameterSet_in_createContextRangePoint1785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AFTER_in_createContextRangePoint1791 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_timePeriod_in_createContextRangePoint1793 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_in_createContextNested1809 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_createContextNested1811 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x00000000BE000000L});
    public static final BitSet FOLLOW_createContextDetail_in_createContextNested1813 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STREAM_EXPR_in_createContextFilter1826 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventFilterExpr_in_createContextFilter1828 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createContextFilter1831 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARTITIONITEM_in_createContextPartitionItem1847 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventFilterExpr_in_createContextPartitionItem1849 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_createContextPartitionItem1852 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_COALESCE_in_createContextCoalesceItem1869 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_libFunctionWithClass_in_createContextCoalesceItem1871 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_eventFilterExpr_in_createContextCoalesceItem1873 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_CTX_CATITEM_in_createContextCategoryItem1889 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_createContextCategoryItem1891 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createContextCategoryItem1893 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_EXPR_in_createExpr1910 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expressionDecl_in_createExpr1912 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_SCHEMA_EXPR_in_createSchemaExpr1931 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_createSchemaDef_in_createSchemaExpr1933 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_createSchemaExpr1935 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CREATE_SCHEMA_DEF_in_createSchemaDef1952 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_createSchemaDef1954 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000003L,0x0000000012000000L,0x0000400000000000L,0x000000000000A001L});
    public static final BitSet FOLLOW_variantList_in_createSchemaDef1957 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_createColTypeList_in_createSchemaDef1959 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_createSchemaQual_in_createSchemaDef1963 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_CREATE_SCHEMA_EXPR_QUAL_in_createSchemaQual1978 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_createSchemaQual1980 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_exprCol_in_createSchemaQual1982 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VARIANT_LIST_in_variantList1998 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_variantList2000 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_insertIntoExpr_in_selectExpr2018 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000080000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_selectClause_in_selectExpr2024 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_fromClause_in_selectExpr2029 = new BitSet(new long[]{0x0000000000000002L,0x0001200000000000L,0xC030000180000000L,0x000000000000001BL});
    public static final BitSet FOLLOW_matchRecogClause_in_selectExpr2034 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L,0xC030000180000000L,0x000000000000001BL});
    public static final BitSet FOLLOW_whereClause_in_selectExpr2041 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L,0xC030000100000000L,0x000000000000001BL});
    public static final BitSet FOLLOW_groupByClause_in_selectExpr2049 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L,0xC020000100000000L,0x000000000000001BL});
    public static final BitSet FOLLOW_havingClause_in_selectExpr2056 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L,0xC020000000000000L,0x000000000000001BL});
    public static final BitSet FOLLOW_outputLimitExpr_in_selectExpr2063 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_orderByClause_in_selectExpr2070 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L});
    public static final BitSet FOLLOW_rowLimitClause_in_selectExpr2077 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSERTINTO_EXPR_in_insertIntoExpr2094 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_insertIntoExpr2096 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_insertIntoExpr2109 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_exprCol_in_insertIntoExpr2112 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPRCOL_in_exprCol2131 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_exprCol2133 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_exprCol2136 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_SELECTION_EXPR_in_selectClause2154 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_selectClause2156 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_DISTINCT_in_selectClause2169 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_selectionList_in_selectClause2172 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_streamExpression_in_fromClause2186 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_streamExpression_in_fromClause2189 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x000F400000000000L});
    public static final BitSet FOLLOW_outerJoin_in_fromClause2192 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x000F400000000000L});
    public static final BitSet FOLLOW_FOR_in_forExpr2212 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_forExpr2214 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_forExpr2216 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_MATCH_RECOGNIZE_in_matchRecogClause2235 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchRecogPartitionBy_in_matchRecogClause2237 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x4000000000000000L});
    public static final BitSet FOLLOW_matchRecogMeasures_in_matchRecogClause2244 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0420000000000000L});
    public static final BitSet FOLLOW_ALL_in_matchRecogClause2250 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0420000000000000L});
    public static final BitSet FOLLOW_matchRecogMatchesAfterSkip_in_matchRecogClause2256 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0420000000000000L});
    public static final BitSet FOLLOW_matchRecogPattern_in_matchRecogClause2262 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x1800000000000000L});
    public static final BitSet FOLLOW_matchRecogMatchesInterval_in_matchRecogClause2268 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x1800000000000000L});
    public static final BitSet FOLLOW_matchRecogDefine_in_matchRecogClause2274 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARTITIONITEM_in_matchRecogPartitionBy2292 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_matchRecogPartitionBy2294 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_MATCHREC_AFTER_SKIP_in_matchRecogMatchesAfterSkip2311 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2315 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2317 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_set_in_matchRecogMatchesAfterSkip2319 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogMatchesAfterSkip2325 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCHREC_INTERVAL_in_matchRecogMatchesInterval2340 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogMatchesInterval2342 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_timePeriod_in_matchRecogMatchesInterval2344 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCHREC_MEASURES_in_matchRecogMeasures2360 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchRecogMeasureListElement_in_matchRecogMeasures2362 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x8000000000000000L});
    public static final BitSet FOLLOW_MATCHREC_MEASURE_ITEM_in_matchRecogMeasureListElement2379 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_matchRecogMeasureListElement2381 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogMeasureListElement2383 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCHREC_PATTERN_in_matchRecogPattern2403 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchRecogPatternAlteration_in_matchRecogPattern2405 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0180000000000000L});
    public static final BitSet FOLLOW_matchRecogPatternConcat_in_matchRecogPatternAlteration2420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MATCHREC_PATTERN_ALTER_in_matchRecogPatternAlteration2428 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchRecogPatternConcat_in_matchRecogPatternAlteration2430 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_matchRecogPatternConcat_in_matchRecogPatternAlteration2432 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_MATCHREC_PATTERN_CONCAT_in_matchRecogPatternConcat2450 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchRecogPatternUnary_in_matchRecogPatternConcat2452 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0240000000000000L});
    public static final BitSet FOLLOW_matchRecogPatternNested_in_matchRecogPatternUnary2467 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_matchRecogPatternAtom_in_matchRecogPatternUnary2472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MATCHREC_PATTERN_NESTED_in_matchRecogPatternNested2487 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchRecogPatternAlteration_in_matchRecogPatternNested2489 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000510000L});
    public static final BitSet FOLLOW_set_in_matchRecogPatternNested2491 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCHREC_PATTERN_ATOM_in_matchRecogPatternAtom2520 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogPatternAtom2522 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000510000L});
    public static final BitSet FOLLOW_set_in_matchRecogPatternAtom2526 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_QUESTION_in_matchRecogPatternAtom2538 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCHREC_DEFINE_in_matchRecogDefine2560 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchRecogDefineItem_in_matchRecogDefine2562 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_MATCHREC_DEFINE_ITEM_in_matchRecogDefineItem2579 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_matchRecogDefineItem2581 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_matchRecogDefineItem2583 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_selectionListElement_in_selectionList2600 = new BitSet(new long[]{0x0000400000000002L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_selectionListElement_in_selectionList2603 = new BitSet(new long[]{0x0000400000000002L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_WILDCARD_SELECT_in_selectionListElement2619 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECTION_ELEMENT_EXPR_in_selectionListElement2629 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_selectionListElement2631 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_selectionListElement2634 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SELECTION_STREAM_in_selectionListElement2648 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_selectionListElement2650 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_selectionListElement2653 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LEFT_OUTERJOIN_EXPR_in_outerJoin2675 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ON_in_outerJoin2677 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_outerJoinIdent_in_outerJoin2679 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RIGHT_OUTERJOIN_EXPR_in_outerJoin2692 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ON_in_outerJoin2694 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_outerJoinIdent_in_outerJoin2696 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FULL_OUTERJOIN_EXPR_in_outerJoin2709 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ON_in_outerJoin2711 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_outerJoinIdent_in_outerJoin2713 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INNERJOIN_EXPR_in_outerJoin2726 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ON_in_outerJoin2728 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_outerJoinIdent_in_outerJoin2730 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_outerJoinIdent2746 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_outerJoinIdent2749 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_outerJoinIdent2753 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_outerJoinIdent2756 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_STREAM_EXPR_in_streamExpression2773 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventFilterExpr_in_streamExpression2776 = new BitSet(new long[]{0x8000000000000008L,0x0000000000000003L,0x0000000010000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_patternInclusionExpression_in_streamExpression2781 = new BitSet(new long[]{0x8000000000000008L,0x0000000000000003L,0x0000000010000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_databaseJoinExpression_in_streamExpression2785 = new BitSet(new long[]{0x8000000000000008L,0x0000000000000003L,0x0000000010000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_methodJoinExpression_in_streamExpression2789 = new BitSet(new long[]{0x8000000000000008L,0x0000000000000003L,0x0000000010000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_viewListExpr_in_streamExpression2793 = new BitSet(new long[]{0x8000000000000008L,0x0000000000000003L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_streamExpression2798 = new BitSet(new long[]{0x8000000000000008L,0x0000000000000003L});
    public static final BitSet FOLLOW_UNIDIRECTIONAL_in_streamExpression2803 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000003L});
    public static final BitSet FOLLOW_set_in_streamExpression2807 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_EXPR_in_eventFilterExpr2832 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_eventFilterExpr2834 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_eventFilterExpr2837 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00001000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_propertyExpression_in_eventFilterExpr2839 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_eventFilterExpr2843 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_EVENT_FILTER_PROPERTY_EXPR_in_propertyExpression2863 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_propertyExpressionAtom_in_propertyExpression2865 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_EVENT_FILTER_PROPERTY_EXPR_ATOM_in_propertyExpressionAtom2884 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SELECT_in_propertyExpressionAtom2888 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_propertySelectionListElement_in_propertyExpressionAtom2890 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x000000000001C000L});
    public static final BitSet FOLLOW_valueExpr_in_propertyExpressionAtom2896 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000080000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000001020L});
    public static final BitSet FOLLOW_ATCHAR_in_propertyExpressionAtom2900 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_propertyExpressionAtom2902 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_propertyExpressionAtom2904 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENT_in_propertyExpressionAtom2909 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_WHERE_EXPR_in_propertyExpressionAtom2913 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_propertyExpressionAtom2915 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PROPERTY_WILDCARD_SELECT_in_propertySelectionListElement2935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROPERTY_SELECTION_ELEMENT_EXPR_in_propertySelectionListElement2945 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_propertySelectionListElement2947 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_propertySelectionListElement2950 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PROPERTY_SELECTION_STREAM_in_propertySelectionListElement2964 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_propertySelectionListElement2966 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_propertySelectionListElement2969 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PATTERN_INCL_EXPR_in_patternInclusionExpression2990 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exprChoice_in_patternInclusionExpression2992 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DATABASE_JOIN_EXPR_in_databaseJoinExpression3009 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_databaseJoinExpression3011 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000001800000L});
    public static final BitSet FOLLOW_set_in_databaseJoinExpression3013 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000001800000L});
    public static final BitSet FOLLOW_set_in_databaseJoinExpression3021 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_METHOD_JOIN_EXPR_in_methodJoinExpression3042 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_methodJoinExpression3044 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_methodJoinExpression3046 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_methodJoinExpression3049 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_viewExpr_in_viewListExpr3063 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_viewExpr_in_viewListExpr3066 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_VIEW_EXPR_in_viewExpr3083 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_viewExpr3085 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_viewExpr3087 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_viewExpr3090 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_WHERE_EXPR_in_whereClause3112 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_whereClause3114 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GROUP_BY_EXPR_in_groupByClause3132 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_groupByClause3134 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_groupByClause3137 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_ORDER_BY_EXPR_in_orderByClause3155 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_orderByElement_in_orderByClause3157 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_orderByElement_in_orderByClause3160 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0040000000000000L});
    public static final BitSet FOLLOW_ORDER_ELEMENT_EXPR_in_orderByElement3180 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_orderByElement3182 = new BitSet(new long[]{0x0600000000000008L});
    public static final BitSet FOLLOW_set_in_orderByElement3184 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_HAVING_EXPR_in_havingClause3207 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_havingClause3209 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_LIMIT_EXPR_in_outputLimitExpr3227 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_outputLimitExpr3229 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000F00000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_number_in_outputLimitExpr3241 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_IDENT_in_outputLimitExpr3243 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAfter_in_outputLimitExpr3246 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAndTerm_in_outputLimitExpr3249 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TIMEPERIOD_LIMIT_EXPR_in_outputLimitExpr3266 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_outputLimitExpr3268 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_timePeriod_in_outputLimitExpr3279 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAfter_in_outputLimitExpr3281 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAndTerm_in_outputLimitExpr3284 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CRONTAB_LIMIT_EXPR_in_outputLimitExpr3300 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_outputLimitExpr3302 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_crontabLimitParameterSet_in_outputLimitExpr3313 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAfter_in_outputLimitExpr3315 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAndTerm_in_outputLimitExpr3318 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHEN_LIMIT_EXPR_in_outputLimitExpr3334 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_outputLimitExpr3336 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_outputLimitExpr3347 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L,0x0100000000000000L});
    public static final BitSet FOLLOW_onSetExpr_in_outputLimitExpr3349 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAfter_in_outputLimitExpr3352 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAndTerm_in_outputLimitExpr3355 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TERM_LIMIT_EXPR_in_outputLimitExpr3371 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_outputLimitExpr3373 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAndTerm_in_outputLimitExpr3384 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L,0x0100000000000000L});
    public static final BitSet FOLLOW_onSetExpr_in_outputLimitExpr3386 = new BitSet(new long[]{0x0000000000000008L,0x0020000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAfter_in_outputLimitExpr3389 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAndTerm_in_outputLimitExpr3392 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AFTER_LIMIT_EXPR_in_outputLimitExpr3405 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_outputLimitAfter_in_outputLimitExpr3407 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_outputLimitAndTerm_in_outputLimitExpr3409 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TERMINATED_in_outputLimitAndTerm3426 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_outputLimitAndTerm3428 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_onSetExpr_in_outputLimitAndTerm3431 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AFTER_in_outputLimitAfter3445 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_timePeriod_in_outputLimitAfter3447 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000F00000000000L});
    public static final BitSet FOLLOW_number_in_outputLimitAfter3450 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ROW_LIMIT_EXPR_in_rowLimitClause3466 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_number_in_rowLimitClause3469 = new BitSet(new long[]{0x0000000000000008L,0x0000400000000000L,0x0000000000000000L,0x0000000000000000L,0x0000F00000000000L,0x0000000000002020L});
    public static final BitSet FOLLOW_IDENT_in_rowLimitClause3471 = new BitSet(new long[]{0x0000000000000008L,0x0000400000000000L,0x0000000000000000L,0x0000000000000000L,0x0000F00000000000L,0x0000000000002020L});
    public static final BitSet FOLLOW_number_in_rowLimitClause3475 = new BitSet(new long[]{0x0000000000000008L,0x0000400000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_IDENT_in_rowLimitClause3477 = new BitSet(new long[]{0x0000000000000008L,0x0000400000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_COMMA_in_rowLimitClause3481 = new BitSet(new long[]{0x0000000000000008L,0x0000400000000000L});
    public static final BitSet FOLLOW_OFFSET_in_rowLimitClause3484 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CRONTAB_LIMIT_EXPR_PARAM_in_crontabLimitParameterSet3502 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3504 = new BitSet(new long[]{0x1020000037CC23C0L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3506 = new BitSet(new long[]{0x1020000037CC23C0L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3508 = new BitSet(new long[]{0x1020000037CC23C0L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3510 = new BitSet(new long[]{0x1020000037CC23C0L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3512 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_crontabLimitParameterSet3514 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LT_in_relationalExpr3531 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_relationalExprValue_in_relationalExpr3533 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GT_in_relationalExpr3546 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_relationalExprValue_in_relationalExpr3548 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LE_in_relationalExpr3561 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_relationalExprValue_in_relationalExpr3563 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GE_in_relationalExpr3575 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_relationalExprValue_in_relationalExpr3577 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_valueExpr_in_relationalExprValue3599 = new BitSet(new long[]{0x1003800037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_relationalExprValue3609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_relationalExprValue3624 = new BitSet(new long[]{0x1000000037CC23C2L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047F07804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_relationalExprValue3633 = new BitSet(new long[]{0x1000000037CC23C2L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_subSelectGroupExpr_in_relationalExprValue3638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EVAL_OR_EXPR_in_evalExprChoice3664 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3666 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3668 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3671 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_EVAL_AND_EXPR_in_evalExprChoice3685 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3687 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3689 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3692 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_EVAL_EQUALS_EXPR_in_evalExprChoice3706 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3708 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3710 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVAL_IS_EXPR_in_evalExprChoice3722 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3724 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3726 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVAL_NOTEQUALS_EXPR_in_evalExprChoice3738 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3740 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3742 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVAL_ISNOT_EXPR_in_evalExprChoice3754 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3756 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3758 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVAL_EQUALS_GROUP_EXPR_in_evalExprChoice3770 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_equalsSubquery_in_evalExprChoice3772 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVAL_IS_GROUP_EXPR_in_evalExprChoice3784 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_equalsSubquery_in_evalExprChoice3786 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVAL_NOTEQUALS_GROUP_EXPR_in_evalExprChoice3798 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_equalsSubquery_in_evalExprChoice3800 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVAL_ISNOT_GROUP_EXPR_in_evalExprChoice3812 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_equalsSubquery_in_evalExprChoice3814 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_EXPR_in_evalExprChoice3826 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_evalExprChoice3828 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_relationalExpr_in_evalExprChoice3839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueExpr_in_equalsSubquery3850 = new BitSet(new long[]{0x0003800000000000L});
    public static final BitSet FOLLOW_set_in_equalsSubquery3852 = new BitSet(new long[]{0x1000000037CC23C2L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047F07804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_equalsSubquery3861 = new BitSet(new long[]{0x1000000037CC23C2L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_subSelectGroupExpr_in_equalsSubquery3866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_valueExpr3880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_substitution_in_valueExpr3886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arithmeticExpr_in_valueExpr3892 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_valueExpr3899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_evalExprChoice_in_valueExpr3908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtinFunc_in_valueExpr3913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_libFuncChain_in_valueExpr3921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_caseExpr_in_valueExpr3926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_inExpr_in_valueExpr3931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_betweenExpr_in_valueExpr3937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_likeExpr_in_valueExpr3942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_regExpExpr_in_valueExpr3947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayExpr_in_valueExpr3952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subSelectInExpr_in_valueExpr3957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subSelectRowExpr_in_valueExpr3963 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subSelectExistsExpr_in_valueExpr3970 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dotExpr_in_valueExpr3975 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_newExpr_in_valueExpr3980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_jsonarray_in_valueExpr3985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_jsonobject_in_valueExpr3991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LAST_in_valueExprWithTime4005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LW_in_valueExprWithTime4014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueExpr_in_valueExprWithTime4021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OBJECT_PARAM_ORDERED_EXPR_in_valueExprWithTime4029 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_valueExprWithTime4031 = new BitSet(new long[]{0x0600000000000000L});
    public static final BitSet FOLLOW_set_in_valueExprWithTime4033 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_rangeOperator_in_valueExprWithTime4046 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_frequencyOperator_in_valueExprWithTime4052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lastOperator_in_valueExprWithTime4057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_weekDayOperator_in_valueExprWithTime4062 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMERIC_PARAM_LIST_in_valueExprWithTime4072 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_numericParameterList_in_valueExprWithTime4074 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000014L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_NUMBERSETSTAR_in_valueExprWithTime4085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_timePeriod_in_valueExprWithTime4092 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_numericParameterList4105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rangeOperator_in_numericParameterList4112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_frequencyOperator_in_numericParameterList4118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMERIC_PARAM_RANGE_in_rangeOperator4134 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_rangeOperator4137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L,0x0000040000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_rangeOperator4140 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L,0x0000040000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_substitution_in_rangeOperator4143 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L,0x0000040000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_rangeOperator4147 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_rangeOperator4150 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_substitution_in_rangeOperator4153 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NUMERIC_PARAM_FREQUENCY_in_frequencyOperator4174 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_frequencyOperator4177 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_frequencyOperator4180 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_substitution_in_frequencyOperator4183 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LAST_OPERATOR_in_lastOperator4202 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_lastOperator4205 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_lastOperator4208 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_substitution_in_lastOperator4211 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WEEKDAY_OPERATOR_in_weekDayOperator4230 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_weekDayOperator4233 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_weekDayOperator4236 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_substitution_in_weekDayOperator4239 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUBSELECT_GROUP_EXPR_in_subSelectGroupExpr4260 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_subQueryExpr_in_subSelectGroupExpr4262 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUBSELECT_EXPR_in_subSelectRowExpr4281 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_subQueryExpr_in_subSelectRowExpr4283 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXISTS_SUBSELECT_EXPR_in_subSelectExistsExpr4302 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_subQueryExpr_in_subSelectExistsExpr4304 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IN_SUBSELECT_EXPR_in_subSelectInExpr4323 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_subSelectInExpr4325 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_subSelectInQueryExpr_in_subSelectInExpr4327 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_IN_SUBSELECT_EXPR_in_subSelectInExpr4339 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_subSelectInExpr4341 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_subSelectInQueryExpr_in_subSelectInExpr4343 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IN_SUBSELECT_QUERY_EXPR_in_subSelectInQueryExpr4362 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_subQueryExpr_in_subSelectInQueryExpr4364 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DISTINCT_in_subQueryExpr4380 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L,0x0000300000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_selectionList_in_subQueryExpr4383 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000400000000000L});
    public static final BitSet FOLLOW_subSelectFilterExpr_in_subQueryExpr4385 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_whereClause_in_subQueryExpr4388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STREAM_EXPR_in_subSelectFilterExpr4406 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventFilterExpr_in_subSelectFilterExpr4408 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000003L,0x0000000010000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_viewListExpr_in_subSelectFilterExpr4412 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000003L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_subSelectFilterExpr4417 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000003L});
    public static final BitSet FOLLOW_RETAINUNION_in_subSelectFilterExpr4421 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000002L});
    public static final BitSet FOLLOW_RETAININTERSECTION_in_subSelectFilterExpr4424 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CASE_in_caseExpr4444 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_caseExpr4447 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_CASE2_in_caseExpr4460 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_caseExpr4463 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_IN_SET_in_inExpr4483 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4485 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000048L});
    public static final BitSet FOLLOW_set_in_inExpr4487 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4493 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0093L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4496 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0093L,0x0000000000000018L});
    public static final BitSet FOLLOW_set_in_inExpr4500 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_IN_SET_in_inExpr4515 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4517 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000048L});
    public static final BitSet FOLLOW_set_in_inExpr4519 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4525 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0093L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4528 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0093L,0x0000000000000018L});
    public static final BitSet FOLLOW_set_in_inExpr4532 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IN_RANGE_in_inExpr4547 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4549 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000048L});
    public static final BitSet FOLLOW_set_in_inExpr4551 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4557 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4559 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_set_in_inExpr4561 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_IN_RANGE_in_inExpr4576 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4578 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000048L});
    public static final BitSet FOLLOW_set_in_inExpr4580 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4586 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_inExpr4588 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_set_in_inExpr4590 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BETWEEN_in_betweenExpr4613 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_betweenExpr4615 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_betweenExpr4617 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_betweenExpr4619 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_BETWEEN_in_betweenExpr4630 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_betweenExpr4632 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_betweenExpr4634 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_betweenExpr4637 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_LIKE_in_likeExpr4657 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_likeExpr4659 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_likeExpr4661 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_likeExpr4664 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_LIKE_in_likeExpr4677 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_likeExpr4679 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_likeExpr4681 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_likeExpr4684 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_REGEXP_in_regExpExpr4703 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_regExpExpr4705 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_regExpExpr4707 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_REGEXP_in_regExpExpr4718 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_regExpExpr4720 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_regExpExpr4722 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUM_in_builtinFunc4741 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4744 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4748 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_aggregationFilterExpr_in_builtinFunc4750 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AVG_in_builtinFunc4762 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4765 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4769 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_aggregationFilterExpr_in_builtinFunc4771 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COUNT_in_builtinFunc4783 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4787 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4791 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_aggregationFilterExpr_in_builtinFunc4795 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MEDIAN_in_builtinFunc4807 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4810 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4814 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_aggregationFilterExpr_in_builtinFunc4816 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STDDEV_in_builtinFunc4828 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4831 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4835 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_aggregationFilterExpr_in_builtinFunc4837 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AVEDEV_in_builtinFunc4849 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4852 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4856 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_aggregationFilterExpr_in_builtinFunc4858 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LAST_AGGREG_in_builtinFunc4870 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4873 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000003C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_accessValueExpr_in_builtinFunc4877 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4880 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FIRST_AGGREG_in_builtinFunc4892 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4895 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000003C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_accessValueExpr_in_builtinFunc4899 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4902 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WINDOW_AGGREG_in_builtinFunc4914 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DISTINCT_in_builtinFunc4917 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_accessValueExpr_in_builtinFunc4921 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COALESCE_in_builtinFunc4934 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4936 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4938 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4941 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_PREVIOUS_in_builtinFunc4956 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4958 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4960 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PREVIOUSTAIL_in_builtinFunc4973 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4975 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4977 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PREVIOUSCOUNT_in_builtinFunc4990 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc4992 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PREVIOUSWINDOW_in_builtinFunc5004 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc5006 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PRIOR_in_builtinFunc5018 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NUM_INT_in_builtinFunc5022 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_builtinFunc5024 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INSTANCEOF_in_builtinFunc5037 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc5039 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_builtinFunc5041 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_builtinFunc5044 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_TYPEOF_in_builtinFunc5058 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc5060 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CAST_in_builtinFunc5072 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_builtinFunc5074 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_builtinFunc5076 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXISTS_in_builtinFunc5088 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_builtinFunc5090 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CURRENT_TIMESTAMP_in_builtinFunc5102 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ISTREAM_in_builtinFunc5115 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_AGG_FILTER_EXPR_in_aggregationFilterExpr5132 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_aggregationFilterExpr5134 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ACCESS_AGG_in_accessValueExpr5148 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_accessValueExprChoice_in_accessValueExpr5150 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PROPERTY_WILDCARD_SELECT_in_accessValueExprChoice5165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROPERTY_SELECTION_STREAM_in_accessValueExprChoice5172 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_accessValueExprChoice5174 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_accessValueExprChoice5176 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_valueExpr_in_accessValueExprChoice5182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARRAY_EXPR_in_arrayExpr5198 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arrayExpr5201 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_PLUS_in_arithmeticExpr5222 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5224 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5226 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MINUS_in_arithmeticExpr5238 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5240 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5242 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DIV_in_arithmeticExpr5254 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5256 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5258 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STAR_in_arithmeticExpr5269 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5271 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5273 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MOD_in_arithmeticExpr5285 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5287 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5289 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BAND_in_arithmeticExpr5300 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5302 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5304 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BOR_in_arithmeticExpr5315 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5317 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5319 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BXOR_in_arithmeticExpr5330 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5332 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5334 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CONCAT_in_arithmeticExpr5346 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5348 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5350 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_arithmeticExpr5353 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_DOT_EXPR_in_dotExpr5373 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_dotExpr5375 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_libFunctionWithClass_in_dotExpr5377 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_NEWKW_in_newExpr5395 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_newAssign_in_newExpr5397 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_NEW_ITEM_in_newAssign5413 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_newAssign5415 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_newAssign5418 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LIB_FUNC_CHAIN_in_libFuncChain5436 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_libFunctionWithClass_in_libFuncChain5438 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0080000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_libOrPropFunction_in_libFuncChain5440 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0080000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_LIB_FUNCTION_in_libFunctionWithClass5460 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_libFunctionWithClass5463 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_libFunctionWithClass5467 = new BitSet(new long[]{0x1020400037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0843L,0x0000000000000018L});
    public static final BitSet FOLLOW_DISTINCT_in_libFunctionWithClass5470 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0843L,0x0000000000000018L});
    public static final BitSet FOLLOW_libFunctionArgItem_in_libFunctionWithClass5474 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0843L,0x0000000000000018L});
    public static final BitSet FOLLOW_LPAREN_in_libFunctionWithClass5477 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_expressionLambdaDecl_in_libFunctionArgItem5491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueExprWithTime_in_libFunctionArgItem5495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_libOrPropFunction5510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_libFunctionWithClass_in_libOrPropFunction5520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_startPatternExpressionRule5535 = new BitSet(new long[]{0x000000000000D800L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_exprChoice_in_startPatternExpressionRule5539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomicExpr_in_exprChoice5553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_patternOp_in_exprChoice5558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EVERY_EXPR_in_exprChoice5568 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exprChoice_in_exprChoice5570 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVERY_DISTINCT_EXPR_in_exprChoice5584 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_distinctExpressions_in_exprChoice5586 = new BitSet(new long[]{0x000000000000D800L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_exprChoice5588 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PATTERN_NOT_EXPR_in_exprChoice5602 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exprChoice_in_exprChoice5604 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GUARD_EXPR_in_exprChoice5618 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exprChoice_in_exprChoice5620 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0023L,0x0000000000000018L});
    public static final BitSet FOLLOW_IDENT_in_exprChoice5623 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_exprChoice5625 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_exprChoice5627 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_exprChoice5632 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCH_UNTIL_EXPR_in_exprChoice5646 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_matchUntilRange_in_exprChoice5648 = new BitSet(new long[]{0x000000000000D800L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_exprChoice5651 = new BitSet(new long[]{0x000000000000D808L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_exprChoice5653 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PATTERN_EVERY_DISTINCT_EXPR_in_distinctExpressions5674 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExprWithTime_in_distinctExpressions5676 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_FOLLOWED_BY_EXPR_in_patternOp5695 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_followedByItem_in_patternOp5697 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_followedByItem_in_patternOp5699 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_followedByItem_in_patternOp5702 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_OR_EXPR_in_patternOp5718 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exprChoice_in_patternOp5720 = new BitSet(new long[]{0x000000000000D800L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_patternOp5722 = new BitSet(new long[]{0x000000000000D808L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_patternOp5725 = new BitSet(new long[]{0x000000000000D808L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_AND_EXPR_in_patternOp5741 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exprChoice_in_patternOp5743 = new BitSet(new long[]{0x000000000000D800L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_patternOp5745 = new BitSet(new long[]{0x000000000000D808L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_patternOp5748 = new BitSet(new long[]{0x000000000000D808L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_FOLLOWED_BY_ITEM_in_followedByItem5769 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_followedByItem5771 = new BitSet(new long[]{0x000000000000D800L,0x0000000000000000L,0x000000000C000340L,0x0800000000000000L});
    public static final BitSet FOLLOW_exprChoice_in_followedByItem5774 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_patternFilterExpr_in_atomicExpr5788 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OBSERVER_EXPR_in_atomicExpr5800 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_atomicExpr5802 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_atomicExpr5804 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExprWithTime_in_atomicExpr5806 = new BitSet(new long[]{0x1020000037CC23C8L,0x100000000001F7E0L,0x008003FC0000003CL,0x0000077707806D00L,0x0007F000000001C4L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_PATTERN_FILTER_EXPR_in_patternFilterExpr5826 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_patternFilterExpr5828 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_CLASS_IDENT_in_patternFilterExpr5831 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00001000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D1003L,0x0000000000000018L});
    public static final BitSet FOLLOW_propertyExpression_in_patternFilterExpr5833 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D1003L,0x0000000000000018L});
    public static final BitSet FOLLOW_patternFilterAnno_in_patternFilterExpr5836 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_patternFilterExpr5840 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_ATCHAR_in_patternFilterAnno5860 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_patternFilterAnno5862 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000F00000000000L});
    public static final BitSet FOLLOW_number_in_patternFilterAnno5864 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCH_UNTIL_RANGE_CLOSED_in_matchUntilRange5879 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_matchUntilRange5881 = new BitSet(new long[]{0x1000000037CC23C0L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_matchUntilRange5883 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCH_UNTIL_RANGE_BOUNDED_in_matchUntilRange5891 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_matchUntilRange5893 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCH_UNTIL_RANGE_HALFCLOSED_in_matchUntilRange5901 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_matchUntilRange5903 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MATCH_UNTIL_RANGE_HALFOPEN_in_matchUntilRange5910 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_matchUntilRange5912 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_PARAM_in_filterParam5925 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_filterParam5927 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_valueExpr_in_filterParam5930 = new BitSet(new long[]{0x1000000037CC23C8L,0x100000000001E7E0L,0x008003FC00000000L,0x0000047707804D00L,0x0007F000000001C0L,0x00000007666D0003L,0x0000000000000018L});
    public static final BitSet FOLLOW_EQUALS_in_filterParamComparator5946 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_filterAtom_in_filterParamComparator5948 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_EQUAL_in_filterParamComparator5955 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_filterAtom_in_filterParamComparator5957 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LT_in_filterParamComparator5964 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_filterAtom_in_filterParamComparator5966 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LE_in_filterParamComparator5973 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_filterAtom_in_filterParamComparator5975 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GT_in_filterParamComparator5982 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_filterAtom_in_filterParamComparator5984 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GE_in_filterParamComparator5991 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_filterAtom_in_filterParamComparator5993 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_RANGE_in_filterParamComparator6000 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6009 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6012 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6016 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6019 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6022 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_NOT_RANGE_in_filterParamComparator6033 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6035 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6042 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6045 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6049 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6052 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6055 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_IN_in_filterParamComparator6066 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6068 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6075 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6078 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6082 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6085 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6089 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_NOT_IN_in_filterParamComparator6100 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6102 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6112 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6116 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6119 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_set_in_filterParamComparator6123 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_BETWEEN_in_filterParamComparator6134 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6140 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6144 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6147 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_FILTER_NOT_BETWEEN_in_filterParamComparator6155 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6158 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000020000L,0x0000000000000000L,0x0007F00000000000L});
    public static final BitSet FOLLOW_constant_in_filterParamComparator6165 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterParamComparator6168 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_constant_in_filterAtom6182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_filterIdentifier_in_filterAtom6188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EVENT_FILTER_IDENT_in_filterIdentifier6199 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_filterIdentifier6201 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0080000000000000L});
    public static final BitSet FOLLOW_eventPropertyExpr_in_filterIdentifier6203 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_PROP_EXPR_in_eventPropertyExpr6222 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_eventPropertyAtomic_in_eventPropertyExpr6224 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x3F00000000000000L});
    public static final BitSet FOLLOW_eventPropertyAtomic_in_eventPropertyExpr6227 = new BitSet(new long[]{0x0000000000000008L,0x0000000000000000L,0x3F00000000000000L});
    public static final BitSet FOLLOW_EVENT_PROP_SIMPLE_in_eventPropertyAtomic6246 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_eventPropertyAtomic6248 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_PROP_INDEXED_in_eventPropertyAtomic6255 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_eventPropertyAtomic6257 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_NUM_INT_in_eventPropertyAtomic6259 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_PROP_MAPPED_in_eventPropertyAtomic6266 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_eventPropertyAtomic6268 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000001800000L});
    public static final BitSet FOLLOW_set_in_eventPropertyAtomic6270 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_PROP_DYNAMIC_SIMPLE_in_eventPropertyAtomic6283 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_eventPropertyAtomic6285 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_PROP_DYNAMIC_INDEXED_in_eventPropertyAtomic6292 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_eventPropertyAtomic6294 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_NUM_INT_in_eventPropertyAtomic6296 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EVENT_PROP_DYNAMIC_MAPPED_in_eventPropertyAtomic6303 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_eventPropertyAtomic6305 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000000000000L,0x0000000001800000L});
    public static final BitSet FOLLOW_set_in_eventPropertyAtomic6307 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TIME_PERIOD_in_timePeriod6332 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_timePeriodDef_in_timePeriod6334 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_yearPart_in_timePeriodDef6350 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x00000000007F0000L});
    public static final BitSet FOLLOW_monthPart_in_timePeriodDef6353 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x00000000007E0000L});
    public static final BitSet FOLLOW_weekPart_in_timePeriodDef6358 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x00000000007C0000L});
    public static final BitSet FOLLOW_dayPart_in_timePeriodDef6363 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000780000L});
    public static final BitSet FOLLOW_hourPart_in_timePeriodDef6368 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000700000L});
    public static final BitSet FOLLOW_minutePart_in_timePeriodDef6373 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000600000L});
    public static final BitSet FOLLOW_secondPart_in_timePeriodDef6378 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_monthPart_in_timePeriodDef6391 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x00000000007E0000L});
    public static final BitSet FOLLOW_weekPart_in_timePeriodDef6394 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x00000000007C0000L});
    public static final BitSet FOLLOW_dayPart_in_timePeriodDef6399 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000780000L});
    public static final BitSet FOLLOW_hourPart_in_timePeriodDef6404 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000700000L});
    public static final BitSet FOLLOW_minutePart_in_timePeriodDef6409 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000600000L});
    public static final BitSet FOLLOW_secondPart_in_timePeriodDef6414 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_weekPart_in_timePeriodDef6427 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x00000000007C0000L});
    public static final BitSet FOLLOW_dayPart_in_timePeriodDef6430 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000780000L});
    public static final BitSet FOLLOW_hourPart_in_timePeriodDef6435 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000700000L});
    public static final BitSet FOLLOW_minutePart_in_timePeriodDef6440 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000600000L});
    public static final BitSet FOLLOW_secondPart_in_timePeriodDef6445 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dayPart_in_timePeriodDef6458 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000780000L});
    public static final BitSet FOLLOW_hourPart_in_timePeriodDef6461 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000700000L});
    public static final BitSet FOLLOW_minutePart_in_timePeriodDef6466 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000600000L});
    public static final BitSet FOLLOW_secondPart_in_timePeriodDef6471 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6476 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hourPart_in_timePeriodDef6483 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000700000L});
    public static final BitSet FOLLOW_minutePart_in_timePeriodDef6486 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000600000L});
    public static final BitSet FOLLOW_secondPart_in_timePeriodDef6491 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_minutePart_in_timePeriodDef6503 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000600000L});
    public static final BitSet FOLLOW_secondPart_in_timePeriodDef6506 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_secondPart_in_timePeriodDef6518 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_millisecondPart_in_timePeriodDef6528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_YEAR_PART_in_yearPart6542 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_yearPart6544 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MONTH_PART_in_monthPart6559 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_monthPart6561 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WEEK_PART_in_weekPart6576 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_weekPart6578 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DAY_PART_in_dayPart6593 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_dayPart6595 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_HOUR_PART_in_hourPart6610 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_hourPart6612 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MINUTE_PART_in_minutePart6627 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_minutePart6629 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SECOND_PART_in_secondPart6644 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_secondPart6646 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MILLISECOND_PART_in_millisecondPart6661 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_valueExpr_in_millisecondPart6663 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUBSTITUTION_in_substitution6678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_TYPE_in_constant6694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LONG_TYPE_in_constant6703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_TYPE_in_constant6712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_TYPE_in_constant6721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_TYPE_in_constant6737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOL_TYPE_in_constant6753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_TYPE_in_constant6766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_number0 = new BitSet(new long[]{0x0000000000000002L});

}