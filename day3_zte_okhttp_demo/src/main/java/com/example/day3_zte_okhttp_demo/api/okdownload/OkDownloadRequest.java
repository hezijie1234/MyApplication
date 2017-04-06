package com.example.day3_zte_okhttp_demo.api.okdownload;


import okhttp3.OkHttpClient;

/**
 * Created by succlz123 on 15/9/11.
 */
public class OkDownloadRequest {
    private int id;

    private String tag;
    private String url;

    private String filePath;

    private long startTime;

    private long finishTime;

    private long fileSize;

    private String fileType;

    private int status;

    private String title;

    private String description;

    private OkHttpClient okHttpClient;

    public OkDownloadRequest() {
    }

    private OkDownloadRequest(Builder builder) {
        id = builder.id;
        tag = builder.tag;
        url = builder.url;
        filePath = builder.filePath;
        fileType = builder.fileType;
        title = builder.title;
        description = builder.description;
        okHttpClient = builder.okHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder {
        private int id;
        private String tag;
        private String url;
        private String filePath;
        private String fileType;
        private String title;
        private String description;
        private OkHttpClient okHttpClient;

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public OkDownloadRequest build() {
            return new OkDownloadRequest(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OkDownloadRequest request = (OkDownloadRequest) o;

        if (getId() != request.getId()) return false;
        if (getStartTime() != request.getStartTime()) return false;
        if (getFinishTime() != request.getFinishTime()) return false;
        if (getFileSize() != request.getFileSize()) return false;
        if (getStatus() != request.getStatus()) return false;
        if (getTag() != null ? !getTag().equals(request.getTag()) : request.getTag() != null)
            return false;
        if (getUrl() != null ? !getUrl().equals(request.getUrl()) : request.getUrl() != null)
            return false;
        if (getFilePath() != null ? !getFilePath().equals(request.getFilePath()) : request.getFilePath() != null)
            return false;
        if (getFileType() != null ? !getFileType().equals(request.getFileType()) : request.getFileType() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(request.getTitle()) : request.getTitle() != null)
            return false;
        return !(getDescription() != null ? !getDescription().equals(request.getDescription()) : request.getDescription() != null);

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getTag() != null ? getTag().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        result = 31 * result + (getFilePath() != null ? getFilePath().hashCode() : 0);
        result = 31 * result + (int) (getStartTime() ^ (getStartTime() >>> 32));
        result = 31 * result + (int) (getFinishTime() ^ (getFinishTime() >>> 32));
        result = 31 * result + (int) (getFileSize() ^ (getFileSize() >>> 32));
        result = 31 * result + (getFileType() != null ? getFileType().hashCode() : 0);
        result = 31 * result + getStatus();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
