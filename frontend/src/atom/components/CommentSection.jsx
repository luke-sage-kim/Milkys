import React, { useState, useEffect } from "react";
import axios from "axios";
import useSessionData from "../../atom/components/MySession";

const CommentSection = ({ parentId, parentType }) => {
    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [newComment, setNewComment] = useState("");
    const [submitError, setSubmitError] = useState("");
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editContent, setEditContent] = useState("");

    const { isLoggedIn, userData } = useSessionData();
    const sessionMemberCode = userData?.memberCode;

    useEffect(() => {
        fetchComments();
    }, [parentId, parentType]);

    const fetchComments = async () => {
        try {
            const response = await axios.post("http://localhost:8080/comment/v1", {
                parent_id: parentId,
                parentType,
            });

            const data = response.data?.resultData;

            if (Array.isArray(data)) {
                setComments(data);
            } else {
                setComments([]);
            }
        } catch (err) {
            setError("댓글을 불러오는 데 실패했습니다.");
        } finally {
            setLoading(false);
        }
    };

    const handleCommentSubmit = async () => {
        if (!isLoggedIn || sessionMemberCode === 0) {
            alert("로그인이 필요합니다.");
            return;
        }

        if (newComment.trim() === "") {
            setSubmitError("내용을 입력해주십시오.");
            return;
        }

        try {
            await axios.post(`http://localhost:8080/comment/v1/${parentId}/comment`, {
                memberCode: sessionMemberCode,
                content: newComment,
                parentType,
            });

            setNewComment("");
            setSubmitError("");
            fetchComments();
        } catch (err) {
            console.error(err);
            setSubmitError("댓글 작성에 실패했습니다.");
        }
    };

    const handleEditClick = (commentId, currentContent) => {
        setEditingCommentId(commentId);
        setEditContent(currentContent);
    };

    const handleEditCancel = () => {
        setEditingCommentId(null);
        setEditContent("");
    };

    const handleEditSubmit = async (commentId) => {
        if (editContent.trim() === "") {
            alert("내용을 입력해주십시오.");
            return;
        }

        try {
            await axios.put(`http://localhost:8080/comment/v1/${commentId}`, {
                memberCode: sessionMemberCode,
                content: editContent,
            });

            setEditingCommentId(null);
            setEditContent("");
            fetchComments();
        } catch (err) {
            console.error(err);
            alert("댓글 수정에 실패했습니다.");
        }
    };

    const handleDelete = async (commentId) => {
        if (!window.confirm("댓글을 삭제하시겠습니까?")) return;

        try {
            await axios.delete(`http://localhost:8080/comment/v1/${commentId}`);
            fetchComments();
        } catch (err) {
            console.error(err);
            alert("댓글 삭제에 실패했습니다.");
        }
    };

    return (
        <div className="mt-10 border-t pt-6">
            <h3 className="text-lg font-semibold mb-4">댓글</h3>

            {/* 로그인한 경우에만 댓글 작성 폼 표시 */}
            {isLoggedIn && (
                <div className="mb-6">
                    <textarea
                        className="w-full p-3 border rounded-lg resize-none"
                        rows="3"
                        placeholder="댓글을 입력하세요..."
                        value={newComment}
                        onChange={(e) => setNewComment(e.target.value)}
                    />
                    {submitError && <p className="text-red-500 text-sm mt-1">{submitError}</p>}
                    <button
                        onClick={handleCommentSubmit}
                        className="mt-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                    >
                        댓글 작성
                    </button>
                </div>
            )}

            {/* 댓글 목록 */}
            {loading ? (
                <div className="text-center text-gray-500">댓글 로딩 중...</div>
            ) : error ? (
                <div className="text-center text-red-500">{error}</div>
            ) : comments.length === 0 ? (
                <p className="text-gray-500">아직 댓글이 없습니다. 첫 댓글을 남겨보세요!</p>
            ) : (
                <ul className="space-y-4">
                    {comments.map((comment) => (
                        <li key={comment.id} className="border p-4 rounded-lg shadow-sm bg-gray-50">
                            <div className="flex justify-between items-center">
                                <div className="font-semibold text-blue-600">
                                    {comment.memberNickName}
                                </div>

                                {/* 수정/삭제 버튼 - 로그인한 사용자이면서 작성자 본인일 때만 */}
                                {isLoggedIn && comment.memberCode === sessionMemberCode && (
                                    <div className="flex gap-2 text-sm text-gray-600">
                                        <button
                                            className="hover:underline"
                                            onClick={() =>
                                                handleEditClick(comment.id, comment.content)
                                            }
                                        >
                                            수정
                                        </button>
                                        <button
                                            className="hover:underline text-red-500"
                                            onClick={() => handleDelete(comment.id)}
                                        >
                                            삭제
                                        </button>
                                    </div>
                                )}
                            </div>

                            {/* 수정 중이면 textarea */}
                            {editingCommentId === comment.id ? (
                                <div className="mt-2">
                                    <textarea
                                        className="w-full p-2 border rounded-lg resize-none"
                                        rows="3"
                                        value={editContent}
                                        onChange={(e) => setEditContent(e.target.value)}
                                    />
                                    <div className="mt-2 flex gap-2">
                                        <button
                                            onClick={() => handleEditSubmit(comment.id)}
                                            className="px-3 py-1 bg-green-500 text-white rounded hover:bg-green-600"
                                        >
                                            저장
                                        </button>
                                        <button
                                            onClick={handleEditCancel}
                                            className="px-3 py-1 bg-gray-300 text-gray-800 rounded hover:bg-gray-400"
                                        >
                                            취소
                                        </button>
                                    </div>
                                </div>
                            ) : (
                                <div className="text-gray-800 mt-1">{comment.content}</div>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default CommentSection;
