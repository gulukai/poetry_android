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
package com.tencentcloudapi.trtc.v20190722.models;

import com.tencentcloudapi.common.AbstractModel;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.util.HashMap;

public class DescribeAbnormalEventResponse extends AbstractModel{

    /**
    * 返回的数据总条数
    */
    @SerializedName("Total")
    @Expose
    private Long Total;

    /**
    * 异常体验列表
    */
    @SerializedName("AbnormalExperienceList")
    @Expose
    private AbnormalExperience [] AbnormalExperienceList;

    /**
    * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
    */
    @SerializedName("RequestId")
    @Expose
    private String RequestId;

    /**
     * Get 返回的数据总条数 
     * @return Total 返回的数据总条数
     */
    public Long getTotal() {
        return this.Total;
    }

    /**
     * Set 返回的数据总条数
     * @param Total 返回的数据总条数
     */
    public void setTotal(Long Total) {
        this.Total = Total;
    }

    /**
     * Get 异常体验列表 
     * @return AbnormalExperienceList 异常体验列表
     */
    public AbnormalExperience [] getAbnormalExperienceList() {
        return this.AbnormalExperienceList;
    }

    /**
     * Set 异常体验列表
     * @param AbnormalExperienceList 异常体验列表
     */
    public void setAbnormalExperienceList(AbnormalExperience [] AbnormalExperienceList) {
        this.AbnormalExperienceList = AbnormalExperienceList;
    }

    /**
     * Get 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。 
     * @return RequestId 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    public String getRequestId() {
        return this.RequestId;
    }

    /**
     * Set 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     * @param RequestId 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    public void setRequestId(String RequestId) {
        this.RequestId = RequestId;
    }

    /**
     * Internal implementation, normal users should not use it.
     */
    public void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "Total", this.Total);
        this.setParamArrayObj(map, prefix + "AbnormalExperienceList.", this.AbnormalExperienceList);
        this.setParamSimple(map, prefix + "RequestId", this.RequestId);

    }
}

