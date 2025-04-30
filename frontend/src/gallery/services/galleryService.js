// src/services/galleryService.js

import axios from "axios";

export const fetchGalleryDetail = async (id, sessionMemberCode, setFormData, navigate) => {
    try {
        const response = await axios.get(`http://localhost:8080/gallery/v1/${id}`);
        const data = response.data.resultData;

        if (data.memberCode !== sessionMemberCode) {
            alert("수정 권한이 없습니다.");
            navigate(-1);
        } else {
            setFormData({
                title: data.title,
                content: data.content
            });
        }
    } catch (err) {
        console.error("갤러리 상세 조회 실패:", err);
        alert("정보를 불러오지 못했습니다.");
        navigate(-1);
    }
};

export const fetchMediaFiles = async (id, setMediaFiles) => {
    try {
        const mediaRequest = {
            domainType: "GALLERY",
            parentId: id
        };
        const response = await axios.post("http://localhost:8080/media/v1/list", mediaRequest);
        if (response.status === 204 || response.data.resultData === "미디어 파일이 없습니다.") {
            setMediaFiles([]);
        } else {
            setMediaFiles(response.data.resultData || []);
        }
    } catch (err) {
        console.error("미디어 파일 조회 실패:", err);
        setMediaFiles([]);
    }
};
