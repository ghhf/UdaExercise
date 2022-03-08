package com.happy.earthquake;

/**
 * JSON 最初设计是为了提高网络的有效沟通，它是一种组织数据的规则，一种完全独立的编程语言，并不是JavaScript，
 *      并且和 JavaScript 没有任何关系。
 *
 *  https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02
 */

public class EarthQuake {

    /**
     * mag : 6.7
     * place : 52km SE of Shizunai, Japan
     * time : 1452741933640
     * updated : 1459304879040
     * tz : 540
     * url : http://earthquake.usgs.gov/earthquakes/eventpage/us10004ebx
     * detail : http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004ebx&format=geojson
     * felt : 51
     * cdi : 5.8
     * mmi : 6.45
     * alert : green
     * status : reviewed
     * tsunami : 1
     * sig : 720
     * net : us
     * code : 10004ebx
     * ids : ,us10004ebx,pt16014050,at00o0xauk,gcmt20160114032534,
     * sources : ,us,pt,at,gcmt,
     * types : ,associate,cap,dyfi,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,
     * nst : null
     * dmin : 0.281
     * rms : 0.98
     * gap : 22
     * magType : mww
     * type : earthquake
     * title : M 6.7 - 52km SE of Shizunai, Japan
     */

    private double mag;
    private String place;
    private long time;
    private long updated;
    private int tz;
    private String url;
    private String detail;
    private int felt;
    private double cdi;
    private double mmi;
    private String alert;
    private String status;
    private int tsunami;
    private int sig;
    private String net;
    private String code;
    private String ids;
    private String sources;
    private String types;
    private Object nst;
    private double dmin;
    private double rms;
    private int gap;
    private String magType;
    private String type;
    private String title;

    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public int getTz() {
        return tz;
    }

    public void setTz(int tz) {
        this.tz = tz;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getFelt() {
        return felt;
    }

    public void setFelt(int felt) {
        this.felt = felt;
    }

    public double getCdi() {
        return cdi;
    }

    public void setCdi(double cdi) {
        this.cdi = cdi;
    }

    public double getMmi() {
        return mmi;
    }

    public void setMmi(double mmi) {
        this.mmi = mmi;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTsunami() {
        return tsunami;
    }

    public void setTsunami(int tsunami) {
        this.tsunami = tsunami;
    }

    public int getSig() {
        return sig;
    }

    public void setSig(int sig) {
        this.sig = sig;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public Object getNst() {
        return nst;
    }

    public void setNst(Object nst) {
        this.nst = nst;
    }

    public double getDmin() {
        return dmin;
    }

    public void setDmin(double dmin) {
        this.dmin = dmin;
    }

    public double getRms() {
        return rms;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public String getMagType() {
        return magType;
    }

    public void setMagType(String magType) {
        this.magType = magType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "EarthQuake{" +
                "mag=" + mag +
                ", place='" + place + '\'' +
                ", time=" + time +
                ", updated=" + updated +
                ", tz=" + tz +
                ", url='" + url + '\'' +
                ", detail='" + detail + '\'' +
                ", felt=" + felt +
                ", cdi=" + cdi +
                ", mmi=" + mmi +
                ", alert='" + alert + '\'' +
                ", status='" + status + '\'' +
                ", tsunami=" + tsunami +
                ", sig=" + sig +
                ", net='" + net + '\'' +
                ", code='" + code + '\'' +
                ", ids='" + ids + '\'' +
                ", sources='" + sources + '\'' +
                ", types='" + types + '\'' +
                ", nst=" + nst +
                ", dmin=" + dmin +
                ", rms=" + rms +
                ", gap=" + gap +
                ", magType='" + magType + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
