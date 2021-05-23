package cn.happy.miwork;

public class Word {
    //-1 超出有效资源的所有可能范围
    private static final int NO_IMAGE_PROVIDED = -1;

    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int mMiwokAudio;
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    public Word(String defaultTranslation, String miwokTranslation,int miwokAudio) {
        this.mDefaultTranslation = defaultTranslation;
        this.mMiwokTranslation = miwokTranslation;
        this.mMiwokAudio = miwokAudio;
    }

    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId,int miwokAudio) {
        this.mDefaultTranslation = defaultTranslation;
        this.mMiwokTranslation = miwokTranslation;
        this.mImageResourceId = imageResourceId;
        this.mMiwokAudio = miwokAudio;

    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public int getMiwokAudio() {
        return mMiwokAudio;
    }

    /**
     * @return 返回当前单词是否有关联的图片
     */
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
