package swd.fpt.exegroupingmanagement.enums;

public enum TeamStatus {
    PENDING, //nhom chua full nguoi
    FULL, //nhom full nguoi
    FAILED, //nhóm kh đủ thành viên khi đóng giai đoạn hoặc bị đánh rớt
    CANCELLED, //nhóm trưởng rã nhóm
    CLOSED, //nhóm đã hoàn thành khóa
    PASSED //nhóm đã hoàn thành khóa và đạt yêu cầu
}
