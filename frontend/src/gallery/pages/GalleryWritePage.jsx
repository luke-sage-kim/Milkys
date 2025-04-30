import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../main/components/ui/Header";
import useSessionData from "../../atom/components/MySession";

const GalleryWritePage = () => {
    const { isLoggedIn, userData } = useSessionData();
    const navigate = useNavigate();

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [files, setFiles] = useState([]);

    const handleFileChange = (e) => {
        setFiles([...e.target.files]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!title || !content) {
            alert("제목과 내용을 모두 입력해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append("memberCode", userData.memberCode); // 로그인 유저의 memberCode 사용
        formData.append("title", title);
        formData.append("content", content);
        files.forEach((file) => {
            formData.append("files", file); // 여러 파일 추가
        });

        try {
            await axios.post("http://localhost:8080/gallery/v1/write", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            alert("작성 완료!");
            navigate("/gallery");
        } catch (error) {
            console.error("작성 중 오류 발생:", error);
            alert("작성에 실패했습니다.");
        }
    };

    return (
        <div className="max-w-3xl mx-auto p-4">
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />
            <h1 className="text-2xl font-bold text-center my-6">갤러리 글 작성</h1>

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-gray-700 font-semibold mb-2">제목</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="w-full border p-2 rounded"
                        placeholder="제목을 입력하세요"
                        required
                    />
                </div>

                <div>
                    <label className="block text-gray-700 font-semibold mb-2">내용</label>
                    <textarea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        className="w-full border p-2 rounded h-40"
                        placeholder="내용을 입력하세요"
                        required
                    ></textarea>
                </div>

                <div>
                    <label className="block text-gray-700 font-semibold mb-2">파일 첨부</label>
                    <input
                        type="file"
                        multiple
                        onChange={handleFileChange}
                        className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                    />
                </div>

                <div className="text-center">
                    <button
                        type="submit"
                        className="bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-6 rounded"
                    >
                        작성하기
                    </button>
                </div>
            </form>
        </div>
    );
};

export default GalleryWritePage;
