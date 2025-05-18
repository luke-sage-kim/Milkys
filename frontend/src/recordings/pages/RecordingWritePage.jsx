import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";

const RecordingWritePage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { isLoggedIn, userData } = useSessionData();

    const [content, setContent] = useState("");
    const [recDate, setRecDate] = useState("");
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleContentChange = (e) => setContent(e.target.value);
    const handleDateChange = (e) => setRecDate(e.target.value);
    const handleFileChange = (e) => setFile(e.target.files[0]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!recDate) {
            alert("날짜를 입력해 주세요.");
            return;
        }

        if (!file) {
            alert("음원 파일을 업로드해 주세요.");
            return;
        }

        setLoading(true);

        const formData = new FormData();
        formData.append("memberCode", userData.memberCode);
        formData.append("parentId", id);
        formData.append("content", content);
        formData.append("recDate", recDate);
        formData.append("files", file); // 주의: files라는 키로 전송

        try {
            await axios.post("http://localhost:8080/recording/v1/write", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            alert("음원이 작성되었습니다.");
            navigate(`/recording-detail/${id}`);
        } catch (error) {
            console.error("음원 작성 실패:", error);
            alert("음원 작성에 실패했습니다.");
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = () => {
        navigate(`/recording-detail/${id}`);
    };

    return (
        <div className="max-w-5xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />
            <h2 className="text-2xl font-bold mb-6">음원 작성</h2>

            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label htmlFor="recDate" className="block text-lg font-semibold mb-2">
                        날짜
                    </label>
                    <input
                        type="date"
                        id="recDate"
                        value={recDate}
                        onChange={handleDateChange}
                        className="w-full p-2 border rounded-lg shadow-sm"
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="content" className="block text-lg font-semibold mb-2">
                        내용
                    </label>
                    <textarea
                        id="content"
                        value={content}
                        onChange={handleContentChange}
                        rows="4"
                        className="w-full p-2 border rounded-lg shadow-sm"
                        placeholder="음원 내용을 작성하세요."
                    ></textarea>
                </div>

                <div className="mb-6">
                    <label htmlFor="file" className="block text-lg font-semibold mb-2">
                        음원 파일 업로드
                    </label>
                    <input
                        type="file"
                        id="file"
                        accept="audio/*"
                        onChange={handleFileChange}
                        className="w-full"
                    />
                </div>

                <div className="flex justify-between">
                    <button
                        type="button"
                        onClick={handleCancel}
                        className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300 transition"
                    >
                        취소
                    </button>

                    <button
                        type="submit"
                        className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
                        disabled={loading}
                    >
                        {loading ? "작성 중..." : "작성하기"}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default RecordingWritePage;
