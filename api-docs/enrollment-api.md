# Enrollment Management APIs

Base URL: `/api/enrollments`

## Overview
Enrollment APIs quản lý việc đăng ký của sinh viên vào các khóa học. **EnrollmentId là key quan trọng để sinh viên có thể tạo team, tham gia team, và tạo ideas.**

## Vai trò của Enrollment:
- **EnrollmentId** đại diện cho sự tham gia của sinh viên vào một course cụ thể
- Mỗi sinh viên có thể có nhiều enrollments (nhiều courses khác nhau)
- Một enrollment chỉ có thể tham gia 1 team trong cùng course
- EnrollmentId được sử dụng trong:
  - **Team Management**: Tạo team, join team, leave team
  - **Idea Management**: Tạo và quản lý ideas
  - **Application**: Apply hoặc nhận invite vào team

## Flow sử dụng Enrollment:
```
1. Sinh viên đăng ký Course → Tạo Enrollment (status: PENDING)
2. Mentor/Admin phê duyệt → Enrollment (status: APPROVED)
3. Sinh viên dùng enrollmentId để:
   - Tạo team mới: POST /api/teams/create?enrollmentId=X
   - Apply vào team: POST /api/applications/apply?enrollmentId=X&teamId=Y
   - Tạo ideas: POST /api/ideas/create?enrollmentId=X
4. Hoàn thành course → Enrollment (status: COMPLETED)
```

## Enrollment Status Flow
```
PENDING → APPROVED → COMPLETED
    ↓
CANCELLED (có thể hủy từ bất kỳ trạng thái nào)
```

---

## Endpoints

### 1. Đăng ký khóa học
**POST** `/api/enrollments`

**Mô tả**: Sinh viên đăng ký vào khóa học

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Request Body**:
```json
{
  "userId": 3,
  "courseId": 1
}
```

**Validation Rules**:
- `userId`: Bắt buộc, phải tồn tại
- `courseId`: Bắt buộc, phải tồn tại
- Không được đăng ký trùng lặp (cùng user, cùng course)
- Khóa học phải có trạng thái ACTIVE
- Số lượng sinh viên chưa vượt quá maxStudents

**Response**:
```json
{
  "success": true,
  "message": "Đăng ký lớp thành công",
  "data": {
    "enrollmentId": 10,
    "userId": 3,
    "userName": "Nguyễn Văn A",
    "userEmail": "student@fpt.edu.vn",
    "courseId": 1,
    "courseName": "Software Development - SE1702",
    "courseCode": "SWD392_SE1702",
    "enrollmentDate": "2024-11-14T10:30:00",
    "status": "PENDING",
    "approvedBy": null,
    "approvedDate": null
  }
}
```

**Important Note**: 
- Sau khi đăng ký thành công, lưu lại `enrollmentId` (VD: 10)
- EnrollmentId này sẽ được dùng để tạo team, join team, và tạo ideas
- Phải chờ mentor approve trước khi có thể tạo/join team

---

### 2. Lấy thông tin đăng ký theo ID
**GET** `/api/enrollments/{id}`

**Mô tả**: Lấy thông tin chi tiết đăng ký theo ID

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `id` (Long): ID của đăng ký (enrollmentId)

**Use Case**: Kiểm tra status của enrollment trước khi tạo team

**Response**:
```json
{
  "success": true,
  "message": "Lấy thông tin đăng ký thành công",
  "data": {
    "enrollmentId": 10,
    "userId": 3,
    "userName": "Nguyễn Văn A",
    "userEmail": "student@fpt.edu.vn",
    "courseId": 1,
    "courseName": "Software Development - SE1702",
    "courseCode": "SWD392_SE1702",
    "enrollmentDate": "2024-11-14T10:30:00",
    "status": "APPROVED",
    "approvedBy": 5,
    "approvedByName": "Nguyễn Văn Thầy",
    "approvedDate": "2024-11-14T11:00:00"
  }
}
```

---

### 3. Lấy danh sách đăng ký theo người dùng
**GET** `/api/enrollments/user/{userId}`

**Mô tả**: Lấy danh sách tất cả đăng ký của một người dùng

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `userId` (Long): ID của người dùng

**Use Case**: Xem tất cả courses mà sinh viên đã enroll

**Response**:
```json
{
  "success": true,
  "message": "Lấy danh sách đăng ký theo người dùng thành công",
  "data": [
    {
      "enrollmentId": 10,
      "userId": 3,
      "userName": "Nguyễn Văn A",
      "courseId": 1,
      "courseName": "Software Development - SE1702",
      "courseCode": "SWD392_SE1702",
      "enrollmentDate": "2024-11-14T10:30:00",
      "status": "APPROVED"
    },
    {
      "enrollmentId": 15,
      "userId": 3,
      "userName": "Nguyễn Văn A",
      "courseId": 2,
      "courseName": "Programming with C# - SE1703",
      "courseCode": "PRN221_SE1703",
      "enrollmentDate": "2024-11-10T14:20:00",
      "status": "PENDING"
    }
  ]
}
```

---

### 4. Lấy danh sách đăng ký theo khóa học
**GET** `/api/enrollments/course/{courseId}`

**Mô tả**: Lấy danh sách tất cả đăng ký của một khóa học

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `courseId` (Long): ID của khóa học

**Use Case**: Mentor xem danh sách sinh viên đã enroll vào course

**Response**:
```json
{
  "success": true,
  "message": "Lấy danh sách đăng ký theo lớp học thành công",
  "data": [
    {
      "enrollmentId": 10,
      "userId": 3,
      "userName": "Nguyễn Văn A",
      "userEmail": "student1@fpt.edu.vn",
      "courseId": 1,
      "courseName": "Software Development - SE1702",
      "enrollmentDate": "2024-11-14T10:30:00",
      "status": "APPROVED"
    },
    {
      "enrollmentId": 12,
      "userId": 5,
      "userName": "Trần Thị B",
      "userEmail": "student2@fpt.edu.vn",
      "courseId": 1,
      "courseName": "Software Development - SE1702",
      "enrollmentDate": "2024-11-13T16:45:00",
      "status": "PENDING"
    }
  ]
}
```

---

### 5. Tìm kiếm đăng ký
**GET** `/api/enrollments/search`

**Mô tả**: Tìm kiếm đăng ký theo userId, courseId hoặc cả hai

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `userId` (Long, optional): ID người dùng
- `courseId` (Long, optional): ID khóa học

**Example**: `/api/enrollments/search?userId=3&courseId=1`

**Use Case**: Kiểm tra xem user đã enroll vào course chưa

**Response**:
```json
{
  "success": true,
  "message": "Tìm kiếm đăng ký thành công (tìm thấy 1 kết quả)",
  "data": [
    {
      "enrollmentId": 10,
      "userId": 3,
      "userName": "Nguyễn Văn A",
      "userEmail": "student@fpt.edu.vn",
      "courseId": 1,
      "courseName": "Software Development - SE1702",
      "courseCode": "SWD392_SE1702",
      "enrollmentDate": "2024-11-14T10:30:00",
      "status": "APPROVED"
    }
  ]
}
```

---

### 6. Phê duyệt đăng ký
**PATCH** `/api/enrollments/{id}/approve`

**Mô tả**: Phê duyệt đăng ký (chỉ Mentor hoặc Admin)

**Yêu cầu**: MENTOR hoặc ADMIN role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `id` (Long): ID của đăng ký

**Query Parameters**:
- `approvedBy` (Long): ID của người phê duyệt (Mentor/Admin)

**Example**: `/api/enrollments/10/approve?approvedBy=5`

**Business Rules**:
- Chỉ enrollment với status PENDING mới có thể approve
- Người phê duyệt phải là mentor của course hoặc admin
- Sau khi approve, sinh viên mới có thể tạo/join team

**Response**:
```json
{
  "success": true,
  "message": "Phê duyệt đăng ký thành công",
  "data": {
    "enrollmentId": 10,
    "userId": 3,
    "userName": "Nguyễn Văn A",
    "userEmail": "student@fpt.edu.vn",
    "courseId": 1,
    "courseName": "Software Development - SE1702",
    "courseCode": "SWD392_SE1702",
    "enrollmentDate": "2024-11-14T10:30:00",
    "status": "APPROVED",
    "approvedBy": 5,
    "approvedByName": "Nguyễn Văn Thầy",
    "approvedDate": "2024-11-14T11:00:00"
  }
}
```

---

### 7. Hoàn thành đăng ký
**PATCH** `/api/enrollments/{id}/complete`

**Mô tả**: Đánh dấu đăng ký hoàn thành (sinh viên đã hoàn thành khóa học)

**Yêu cầu**: MENTOR hoặc ADMIN role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `id` (Long): ID của đăng ký

**Business Rules**:
- Chỉ enrollment với status APPROVED mới có thể complete
- Sau khi complete, không thể tạo team mới hoặc join team

**Response**:
```json
{
  "success": true,
  "message": "Hoàn thành đăng ký",
  "data": {
    "enrollmentId": 10,
    "userId": 3,
    "userName": "Nguyễn Văn A",
    "courseId": 1,
    "courseName": "Software Development - SE1702",
    "status": "COMPLETED",
    "completedDate": "2024-12-20T10:00:00"
  }
}
```

---

### 8. Hủy đăng ký
**DELETE** `/api/enrollments/{id}`

**Mô tả**: Hủy đăng ký khóa học

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `id` (Long): ID của đăng ký

**Business Rules**:
- Nếu sinh viên đã có team, phải leave team trước khi hủy enrollment
- Sau khi hủy, không thể tạo team hoặc ideas với enrollmentId này

**Response**:
```json
{
  "success": true,
  "message": "Hủy đăng ký thành công"
}
```

---

## Complete Workflow: Từ Enrollment đến Team

### Step 1: Sinh viên xem courses available
```http
GET /api/courses/semester/2
Authorization: Bearer <student_token>

Response: Danh sách courses trong FALL2024
```

### Step 2: Sinh viên enroll vào course
```http
POST /api/enrollments
Authorization: Bearer <student_token>

{
  "userId": 3,
  "courseId": 1
}

Response:
{
  "enrollmentId": 10,
  "status": "PENDING"
}
```

### Step 3: Kiểm tra status của enrollment
```http
GET /api/enrollments/10
Authorization: Bearer <student_token>

Response:
{
  "enrollmentId": 10,
  "status": "PENDING"  // Chưa được approve, chưa thể tạo team
}
```

### Step 4: Mentor approve enrollment
```http
PATCH /api/enrollments/10/approve?approvedBy=5
Authorization: Bearer <mentor_token>

Response:
{
  "enrollmentId": 10,
  "status": "APPROVED"  // Đã approve, có thể tạo team
}
```

### Step 5: Sinh viên tạo team với enrollmentId
```http
POST /api/teams/create?enrollmentId=10&teamName=Team Alpha
Authorization: Bearer <student_token>

Response:
{
  "teamId": 1,
  "name": "Team Alpha",
  "members": [
    {
      "enrollmentId": 10,
      "userId": 3,
      "isLeader": true
    }
  ]
}
```

### Step 6: Hoặc sinh viên apply vào team khác
```http
POST /api/applications/apply?enrollmentId=10&teamId=2
Authorization: Bearer <student_token>

Response:
{
  "applicationId": 1,
  "enrollmentId": 10,
  "teamId": 2,
  "status": "PENDING"
}
```

### Step 7: Sinh viên tạo ideas với enrollmentId
```http
POST /api/ideas/create?enrollmentId=10&name=My Idea&description=...
Authorization: Bearer <student_token>

Response:
{
  "ideaId": 5,
  "enrollment": {
    "enrollmentId": 10
  }
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Sinh viên đã đăng ký khóa học này"
}
```

```json
{
  "success": false,
  "message": "Khóa học đã đầy (30/30 sinh viên)"
}
```

```json
{
  "success": false,
  "message": "Khóa học không còn mở đăng ký"
}
```

```json
{
  "success": false,
  "message": "Enrollment chưa được approve, không thể tạo team"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Bạn không có quyền phê duyệt đăng ký này"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Không tìm thấy đăng ký với ID: 999"
}
```

### 409 Conflict
```json
{
  "success": false,
  "message": "Không thể hủy enrollment vì đang tham gia team"
}
```

---

## Notes
- **EnrollmentId là key quan trọng nhất** trong hệ thống team management
- Một user có thể có nhiều enrollments (nhiều courses)
- Một enrollment chỉ có thể tham gia 1 team trong cùng course
- Enrollment phải được APPROVED trước khi có thể tạo/join team
- Khi hủy enrollment, phải leave team trước (nếu đang trong team)
- Status COMPLETED: Không thể tạo team mới hoặc join team
- `currentStudents` của course được tính từ số enrollments APPROVED
- Mentor chỉ có thể approve enrollments của courses mà họ dạy