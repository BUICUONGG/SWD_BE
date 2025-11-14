# Course Management APIs

Base URL: `/api/courses`

## Overview
Course Management APIs quản lý các khóa học/lớp học trong hệ thống. **Sinh viên phải đăng ký (enroll) vào course trước khi có thể tạo team hoặc tham gia team.**

## Flow tạo Team:
```
1. Admin/Mentor tạo Course
2. Sinh viên Enroll vào Course → nhận enrollmentId
3. Sinh viên dùng enrollmentId để:
   - Tạo Team mới (trở thành Leader)
   - Hoặc Apply/Accept invite vào Team khác
4. Trong Team có thể tạo Ideas và làm dự án
```

## Vai trò trong hệ thống:
- **Course** là container chứa tất cả enrollments, teams, và ideas
- Mỗi course thuộc về 1 semester và 1 subject
- Course có thể có 1 mentor (giảng viên phụ trách)
- Course có giới hạn số lượng sinh viên (maxStudents)
- Course có deadline tạo team (teamFormationDeadline)

---

## Endpoints

### 1. Tạo khóa học mới
**POST** `/api/courses`

**Mô tả**: Tạo khóa học/lớp học mới

**Yêu cầu**: ADMIN hoặc MENTOR role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Request Body**:
```json
{
  "code": "SWD392_SE1702",
  "name": "Software Development - SE1702",
  "maxStudents": 30,
  "teamFormationDeadline": "2024-12-15T23:59:59",
  "status": "ACTIVE",
  "mentorId": 5,
  "subjectId": 1,
  "semesterId": 2
}
```

**Validation Rules**:
- `code`: Bắt buộc, tối đa 20 ký tự, duy nhất
- `name`: Bắt buộc, tối đa 200 ký tự  
- `maxStudents`: Phải lớn hơn 0
- `subjectId`: Bắt buộc, phải tồn tại
- `semesterId`: Bắt buộc, phải tồn tại
- `status`: ACTIVE, INACTIVE, COMPLETED, CANCELLED
- `teamFormationDeadline`: Định dạng ISO datetime (yyyy-MM-dd'T'HH:mm:ss)

**Response**:
```json
{
  "success": true,
  "message": "Tạo lớp học thành công",
  "data": {
    "courseId": 1,
    "code": "SWD392_SE1702",
    "name": "Software Development - SE1702",
    "maxStudents": 30,
    "currentStudents": 0,
    "teamFormationDeadline": "2024-12-15T23:59:59",
    "status": "ACTIVE",
    "mentorId": 5,
    "mentorName": "Nguyễn Văn Thầy",
    "subjectId": 1,
    "subjectCode": "SWD392",
    "semesterId": 2,
    "semesterCode": "FALL2024"
  }
}
```

---

### 2. Lấy thông tin khóa học theo ID
**GET** `/api/courses/{id}`

**Mô tả**: Lấy thông tin chi tiết khóa học theo ID

**Yêu cầu**: Authenticated

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `id` (Long): ID của khóa học

**Response**:
```json
{
  "success": true,
  "message": "Lấy thông tin lớp học thành công",
  "data": {
    "courseId": 1,
    "code": "SWD392_SE1702",
    "name": "Software Development - SE1702",
    "maxStudents": 30,
    "currentStudents": 25,
    "teamFormationDeadline": "2024-12-15T23:59:59",
    "status": "ACTIVE",
    "mentorId": 5,
    "mentorName": "Nguyễn Văn Thầy",
    "subjectId": 1,
    "subjectCode": "SWD392",
    "semesterId": 2,
    "semesterCode": "FALL2024"
  }
}
```

---

### 3. Lấy thông tin khóa học theo mã
**GET** `/api/courses/code/{code}`

**Mô tả**: Lấy thông tin khóa học theo mã khóa học

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `code` (String): Mã khóa học

**Example**: `/api/courses/code/SWD392_SE1702`

**Response**: Tương tự GET by ID

---

### 4. Lấy danh sách tất cả khóa học
**GET** `/api/courses`

**Mô tả**: Lấy danh sách tất cả khóa học (dùng để sinh viên chọn course để enroll)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Response**:
```json
{
  "success": true,
  "message": "Lấy danh sách lớp học thành công",
  "data": [
    {
      "courseId": 1,
      "code": "SWD392_SE1702",
      "name": "Software Development - SE1702",
      "maxStudents": 30,
      "currentStudents": 25,
      "status": "ACTIVE",
      "mentorName": "Nguyễn Văn Thầy",
      "subjectCode": "SWD392",
      "semesterCode": "FALL2024"
    },
    {
      "courseId": 2,
      "code": "PRN221_SE1703",
      "name": "Programming with C# - SE1703",
      "maxStudents": 25,
      "currentStudents": 20,
      "status": "ACTIVE",
      "mentorName": "Trần Thị Cô",
      "subjectCode": "PRN221",
      "semesterCode": "FALL2024"
    }
  ]
}
```

---

### 5. Tìm kiếm khóa học với nhiều tiêu chí
**GET** `/api/courses/search`

**Mô tả**: Tìm kiếm khóa học theo từ khóa, trạng thái, kỳ học, mentor, môn học

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `keyword` (String, optional): Từ khóa tìm kiếm (code hoặc name)
- `status` (CourseStatus, optional): Trạng thái (ACTIVE, INACTIVE, COMPLETED, CANCELLED)
- `semesterId` (Long, optional): ID kỳ học
- `mentorId` (Long, optional): ID mentor
- `subjectId` (Long, optional): ID môn học

**Example**: `/api/courses/search?keyword=SWD&status=ACTIVE&semesterId=2`

**Response**:
```json
{
  "success": true,
  "message": "Tìm kiếm lớp học thành công (tìm thấy 3 kết quả)",
  "data": [
    {
      "courseId": 1,
      "code": "SWD392_SE1702",
      "name": "Software Development - SE1702",
      "maxStudents": 30,
      "currentStudents": 25,
      "status": "ACTIVE",
      "mentorName": "Nguyễn Văn Thầy",
      "subjectCode": "SWD392",
      "semesterCode": "FALL2024"
    }
  ]
}
```

---

### 6. Lấy khóa học theo trạng thái
**GET** `/api/courses/status/{status}`

**Mô tả**: Lấy danh sách khóa học theo trạng thái

**Path Parameters**:
- `status` (CourseStatus): Trạng thái (ACTIVE, INACTIVE, COMPLETED, CANCELLED)

**Example**: `/api/courses/status/ACTIVE`

---

### 7. Lấy khóa học theo kỳ học
**GET** `/api/courses/semester/{semesterId}`

**Mô tả**: Lấy danh sách khóa học theo kỳ học (dùng để sinh viên xem courses có thể enroll)

**Path Parameters**:
- `semesterId` (Long): ID kỳ học

**Use Case**: Sinh viên chọn semester hiện tại, xem tất cả courses available để enroll

---

### 8. Lấy khóa học theo mentor
**GET** `/api/courses/mentor/{mentorId}`

**Mô tả**: Lấy danh sách khóa học được dạy bởi mentor

**Path Parameters**:
- `mentorId` (Long): ID của mentor

---

### 9. Cập nhật khóa học
**PUT** `/api/courses/{id}`

**Mô tả**: Cập nhật thông tin khóa học

**Yêu cầu**: ADMIN hoặc MENTOR role

**Request Body**: Tương tự POST

---

### 10. Xóa khóa học
**DELETE** `/api/courses/{id}`

**Mô tả**: Xóa khóa học

**Yêu cầu**: ADMIN role

**Response**:
```json
{
  "success": true,
  "message": "Xóa lớp học thành công"
}
```

---

## Complete Workflow: Từ Course đến Team

### Step 1: Admin tạo Course
```http
POST /api/courses
Authorization: Bearer <admin_token>

{
  "code": "SWD392_SE1702",
  "name": "Software Development - SE1702",
  "maxStudents": 30,
  "teamFormationDeadline": "2024-12-15T23:59:59",
  "status": "ACTIVE",
  "mentorId": 5,
  "subjectId": 1,
  "semesterId": 2
}

Response: courseId = 1
```

### Step 2: Sinh viên xem danh sách courses
```http
GET /api/courses/semester/2
Authorization: Bearer <student_token>

Response: Danh sách courses trong semester FALL2024
```

### Step 3: Sinh viên enroll vào course
```http
POST /api/enrollments
Authorization: Bearer <student_token>

{
  "userId": 3,
  "courseId": 1
}

Response: enrollmentId = 10 (PENDING)
```

### Step 4: Mentor phê duyệt enrollment
```http
PATCH /api/enrollments/10/approve?approvedBy=5
Authorization: Bearer <mentor_token>

Response: enrollment status = APPROVED
```

### Step 5: Sinh viên tạo team
```http
POST /api/teams/create?enrollmentId=10&teamName=Team Alpha
Authorization: Bearer <student_token>

Response: teamId = 1, sinh viên trở thành leader
```

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Mã lớp không được để trống"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Không tìm thấy khóa học với ID: 999"
}
```

### 409 Conflict
```json
{
  "success": false,
  "message": "Mã khóa học đã tồn tại"
}
```

```json
{
  "success": false,
  "message": "Khóa học đã đầy (30/30 sinh viên)"
}
```

---

## Notes
- `currentStudents` được tính tự động dựa trên số lượng enrollments được APPROVED
- Khi xóa khóa học, tất cả enrollments, teams, ideas liên quan cũng sẽ bị ảnh hưởng
- `teamFormationDeadline`: Sau deadline này, sinh viên không thể tạo team mới
- Status COMPLETED: Course đã kết thúc, không thể enroll hoặc tạo team
- Status ACTIVE: Course đang mở, sinh viên có thể enroll và tạo team
- Mentor phải có role MENTOR hoặc ADMIN
- Chỉ courses với status ACTIVE mới cho phép sinh viên enroll