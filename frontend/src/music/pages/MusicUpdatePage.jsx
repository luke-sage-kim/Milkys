import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import useSessionData from "../../atom/components/MySession";

const MusicUpdatePage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { userData } = useSessionData();
    const memberCode = userData?.memberCode;

    const [formData, setFormData] = useState({
        title: "",
        content: "",
        musicLink: ""
    });

    useEffect(() => {
        const fetchMusicDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/music/v1/${id}`);
                const data = response.data.resultData;
                if (data.memberCode !== memberCode) {
                    alert("수정 권한이 없습니다.");
                    navigate(-1);
                } else {
                    setFormData({
                        title: data.title,
                        content: data.content,
                        musicLink: data.musicLink
                    });
                }
            } catch (err) {
                console.error("Error fetching music details:", err);
                alert("음악 정보를 불러오는 데 실패했습니다.");
                navigate(-1);
            }
        };

        if (memberCode) {
            fetchMusicDetail();
        }
    }, [id, memberCode, navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:8080/music/v1/${id}`, {
                ...formData,
                memberCode
            });
            alert("음악 정보가 수정되었습니다.");
            navigate(`/music-detail/${id}`);
        } catch (err) {
            console.error("Error updating music:", err);
            alert("음악 정보 수정에 실패했습니다.");
        }
    };

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white shadow-lg rounded-lg">
            <h2 className="text-2xl font-bold text-center mt-4">음악 수정</h2>
            <form onSubmit={handleSubmit} className="mt-6 flex flex-col space-y-4">
                <div>
                    <label className="block font-semibold">제목</label>
                    <input
                        type="text"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                </div>
                <div>
                    <label className="block font-semibold">내용</label>
                    <textarea
                        name="content"
                        value={formData.content}
                        onChange={handleChange}
                        className="w-full px-4 py-2 border rounded-lg"
                        required
                    />
                </div>
                <div>
                    <label className="block font-semibold">유튜브 링크</label>
                    <input
                        type="text"
                        name="musicLink"
                        value={formData.musicLink}
                        onChange={handleChange}
                        className="w-full px-4 py-2 border rounded-lg"
                    />
                </div>
                <button type="submit" className="mt-4 bg-blue-500 text-white px-4 py-2 rounded-lg shadow-lg hover:bg-blue-600">
                    수정하기
                </button>
            </form>
        </div>
    );
};

export default MusicUpdatePage;
