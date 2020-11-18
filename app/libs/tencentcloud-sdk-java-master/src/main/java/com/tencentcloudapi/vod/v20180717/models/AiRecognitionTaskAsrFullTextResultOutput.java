/*
 * Copyright (c) 2017-2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tencentcloudapi.vod.v20180717.models;

import com.tencentcloudapi.common.AbstractModel;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.util.HashMap;

public class AiRecognitionTaskAsrFullTextResultOutput extends AbstractModel{

    /**
    * 语音全文识别片段列表。
    */
    @SerializedName("SegmentSet")
    @Expose
    private AiRecognitionTaskAsrFullTextSegmentItem [] SegmentSet;

    /**
    * 字幕文件 Url。
    */
    @SerializedName("SubtitleUrl")
    @Expose
    private String SubtitleUrl;

    /**
     * Get 语音全文识别片段列表。 
     * @return SegmentSet 语音全文识别片段列表。
     */
    public AiRecognitionTaskAsrFullTextSegmentItem [] getSegmentSet() {
        return this.SegmentSet;
    }

    /**
     * Set 语音全文识别片段列表。
     * @param SegmentSet 语音全文识别片段列表。
     */
    public void setSegmentSet(AiRecognitionTaskAsrFullTextSegmentItem [] SegmentSet) {
        this.SegmentSet = SegmentSet;
    }

    /**
     * Get 字幕文件 Url。 
     * @return SubtitleUrl 字幕文件 Url。
     */
    public String getSubtitleUrl() {
        return this.SubtitleUrl;
    }

    /**
     * Set 字幕文件 Url。
     * @param SubtitleUrl 字幕文件 Url。
     */
    public void setSubtitleUrl(String SubtitleUrl) {
        this.SubtitleUrl = SubtitleUrl;
    }

    /**
     * Internal implementation, normal users should not use it.
     */
    public void toMap(HashMap<String, String> map, String prefix) {
        this.setParamArrayObj(map, prefix + "SegmentSet.", this.SegmentSet);
        this.setParamSimple(map, prefix + "SubtitleUrl", this.SubtitleUrl);

    }
}
