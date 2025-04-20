import React, { useState } from "react";
import axios from "axios";
import Header from "../../main/components/ui/Header"; // Header 컴포넌트 import
import useSessionData from "../../atom/components/MySession"; // 세션 훅 import
import { useNavigate } from 'react-router-dom'; // useNavigate 훅을 가져옵니다

const MusicWritePage = () => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [musicLink, setMusicLink] = useState("");
    const [error, setError] = useState("");

    // 세션 데이터 가져오기
    const { isLoggedIn, userData } = useSessionData();

    // react-router-dom의 useNavigate 훅 사용
    const navigate = useNavigate();

    // 입력값 검증 및 DTO에 memberId 추가
    const handleSubmit = async (e) => {
        e.preventDefault();

        // 필수 항목들 체크
        if (!title || !content || !musicLink) {
            setError("모든 필드를 입력해 주세요.");
            return;
        }

        // 세션에서 가져온 memberId를 DTO에 추가
        const writeMusicDto = {
            title,
            content,
            musicLink,
            memberId: userData.memberId, // 세션에서 가져온 memberId 추가
        };

        try {
            // 서버로 데이터 전송
            const response = await axios.post("http://localhost:8080/music/v1", writeMusicDto, {
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.status === 200) {
                alert("음악 정보가 성공적으로 작성되었습니다.");
                // 성공 시 /music-main으로 이동
                navigate('/music-main');
            }
        } catch (err) {
            setError("음악 작성에 실패했습니다. 다시 시도해 주세요.");
            console.error("Error during music write:", err);
        }
    };

    // 로그인 되어 있지 않으면 작성 페이지 접근을 막을 수 있음
    if (!isLoggedIn) {
        return <div>로그인 후 이용 가능합니다.</div>;
    }

    return (
        <div>
            {/* Header 컴포넌트에 로그인 상태와 사용자 정보를 전달 */}
            <Header isLoggedIn={isLoggedIn} nickname={userData.memberNickname} />

            <h2>음악 정보 작성</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">제목</label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="content">내용</label>
                    <textarea
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="musicLink">음악 링크</label>
                    <input
                        type="text"
                        id="musicLink"
                        value={musicLink}
                        onChange={(e) => setMusicLink(e.target.value)}
                        required
                    />
                </div>
                {error && <p style={{ color: "red" }}>{error}</p>}
                <div>
                    <button type="submit">작성하기</button>
                    <button
                        type="button"
                        onClick={() => navigate('/music-main')} // 취소 시 /music-main으로 이동
                    >
                        작성하기 취소
                    </button>
                </div>
            </form>
        </div>
    );
};

export default MusicWritePage;
