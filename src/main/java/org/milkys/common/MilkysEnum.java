package org.milkys.common;

public class MilkysEnum {

    public enum MemberRoleType {
        ADMIN,
        USER,
        LOCAMANAGER
    }

    public enum BoardType {
        NOTICE,
        REVIEW,
        FLASH,
        FREE
    }

    public enum MusicStatus {
        SETLIST,
        SHARE
    }

    public enum CommentParent {
        MUSIC,
        BOARD,
        GALLERY,
        RECORDINGS
    }
}
