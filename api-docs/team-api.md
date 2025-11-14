# Team Management APIs

Base URL: `/api/teams`

## Overview
Team Management APIs cho phép sinh viên tạo và quản lý team trong các khóa học. Mỗi team có một leader (người tạo team) và có thể chứa nhiều members. Team có thể chọn một idea chính từ các ideas của members.

## Flow hoạt động

### 1. Quy trình tạo và quản lý team:
```
1. Sinh viên đăng ký khóa học → có enrollmentId
2. Sinh viên tạo team (trở thành leader)
3. Các sinh viên khác có thể tham gia team (qua API khác hoặc invite)
4. Mỗi member có thể tạo ideas cho team
5. Leader chọn 1 idea làm idea chính cho team
6. Leader có thể: đổi tên team, kick members, chọn idea, giải tán team
7. Members có thể: rời team (trừ leader)
```

### 2. Quyền hạn:
- **Leader**: Tạo team, chọn idea chính, đổi tên team, kick members, giải tán team
- **Members**: Xem thông tin team, rời team, tạo ideas
- **Mentor**: Xem tất cả teams trong khóa học của mình

---

## Endpoints

### 1. Tạo team mới
**POST** `/api/teams/create`

**Mô tả**: Sinh viên tạo team mới cho một enrollment. Người tạo tự động trở thành team leader.

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `enrollmentId` (Long, required): ID của enrollment (sinh viên phải đăng ký khóa học trước)
- `teamName` (String, required): Tên của team

**Example**: `/api/teams/create?enrollmentId=1&teamName=Team Alpha`

**Business Rules**:
- Một enrollment chỉ có thể tham gia 1 team duy nhất trong cùng một khóa học
- Enrollment phải tồn tại và thuộc về user hiện tại
- Team name không được để trống

**Response**:
```json
{
  "success": true,
  "message": "Tạo team thành công",
  "data": {
    "id": 1,
    "name": "Team Alpha",
    "courseId": 5,
    "courseName": "Software Development - SE1702",
    "courseCode": "SWD392_SE1702",
    "semesterId": 2,
    "semesterName": "Fall Semester 2024",
    "mentorId": 10,
    "mentorName": "Nguyễn Văn Thầy",
    "memberCount": 1,
    "mainIdeaId": null,
    "mainIdeaName": null,
    "members": [
      {
        "enrollmentId": 1,
        "userId": 3,
        "userFullName": "Nguyễn Văn A",
        "userEmail": "student1@fpt.edu.vn",
        "isLeader": true,
        "majorName": "Software Engineering"
      }
    ],
    "ideas": []
  }
}
```

---

### 2. Xem chi tiết team
**GET** `/api/teams/{teamId}`

**Mô tả**: Lấy thông tin chi tiết của team bao gồm members và ideas

**Yêu cầu**: Authenticated user

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team

**Example**: `/api/teams/1`

**Response**:
```json
{
  "success": true,
  "message": "Lấy thông tin team thành công",
  "data": {
    "id": 1,
    "name": "Team Alpha",
    "courseId": 5,
    "courseName": "Software Development - SE1702",
    "courseCode": "SWD392_SE1702",
    "semesterId": 2,
    "semesterName": "Fall Semester 2024",
    "mentorId": 10,
    "mentorName": "Nguyễn Văn Thầy",
    "memberCount": 3,
    "mainIdeaId": 5,
    "mainIdeaName": "Student Management System",
    "members": [
      {
        "enrollmentId": 1,
        "userId": 3,
        "userFullName": "Nguyễn Văn A",
        "userEmail": "student1@fpt.edu.vn",
        "isLeader": true,
        "majorName": "Software Engineering"
      },
      {
        "enrollmentId": 8,
        "userId": 12,
        "userFullName": "Trần Thị B",
        "userEmail": "student2@fpt.edu.vn",
        "isLeader": false,
        "majorName": "Artificial Intelligence"
      },
      {
        "enrollmentId": 15,
        "userId": 20,
        "userFullName": "Lê Văn C",
        "userEmail": "student3@fpt.edu.vn",
        "isLeader": false,
        "majorName": "Software Engineering"
      }
    ],
    "ideas": [
      {
        "ideaId": 5,
        "name": "Student Management System",
        "description": "Hệ thống quản lý sinh viên với đầy đủ tính năng",
        "ownerId": 3,
        "ownerName": "Nguyễn Văn A",
        "isMainIdea": true
      },
      {
        "ideaId": 7,
        "name": "Online Learning Platform",
        "description": "Nền tảng học trực tuyến",
        "ownerId": 12,
        "ownerName": "Trần Thị B",
        "isMainIdea": false
      },
      {
        "ideaId": 9,
        "name": "E-commerce Website",
        "description": "Website thương mại điện tử",
        "ownerId": 20,
        "ownerName": "Lê Văn C",
        "isMainIdea": false
      }
    ]
  }
}
```

---

### 3. Xem các team của tôi
**GET** `/api/teams/my-teams`

**Mô tả**: Lấy danh sách tất cả các team mà user hiện tại đang tham gia

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Response**:
```json
{
  "success": true,
  "message": "Lấy danh sách team của bạn thành công",
  "data": [
    {
      "id": 1,
      "name": "Team Alpha",
      "courseId": 5,
      "courseName": "Software Development - SE1702",
      "courseCode": "SWD392_SE1702",
      "semesterId": 2,
      "semesterName": "Fall Semester 2024",
      "mentorId": 10,
      "mentorName": "Nguyễn Văn Thầy",
      "memberCount": 3,
      "mainIdeaId": 5,
      "mainIdeaName": "Student Management System",
      "members": [...],
      "ideas": [...]
    },
    {
      "id": 3,
      "name": "Team Beta",
      "courseId": 8,
      "courseName": "Programming with C# - SE1703",
      "courseCode": "PRN221_SE1703",
      "semesterId": 2,
      "semesterName": "Fall Semester 2024",
      "mentorId": 11,
      "mentorName": "Trần Thị Cô",
      "memberCount": 4,
      "mainIdeaId": 12,
      "mainIdeaName": "Hospital Management System",
      "members": [...],
      "ideas": [...]
    }
  ]
}
```

---

### 4. Xem team theo enrollment
**GET** `/api/teams/by-enrollment`

**Mô tả**: Lấy thông tin team của một enrollment cụ thể

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `enrollmentId` (Long, required): ID của enrollment

**Example**: `/api/teams/by-enrollment?enrollmentId=1`

**Response**:
```json
{
  "success": true,
  "message": "string (auto-generated)",
  "data": [
    {
      "id": 1,
      "name": "Team Alpha",
      "courseId": 5,
      "courseName": "Software Development - SE1702",
      "courseCode": "SWD392_SE1702",
      "semesterId": 2,
      "semesterName": "Fall Semester 2024",
      "mentorId": 10,
      "mentorName": "Nguyễn Văn Thầy",
      "memberCount": 3,
      "mainIdeaId": 5,
      "mainIdeaName": "Student Management System",
      "members": [...],
      "ideas": [...]
    }
  ]
}
```

---

### 5. Xem tất cả teams trong khóa học (Mentor)
**GET** `/api/teams`

**Mô tả**: Mentor xem tất cả các teams trong khóa học của mình

**Yêu cầu**: Authenticated user (thường là MENTOR hoặc ADMIN)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `CourseId` (Long, required): ID của khóa học
- `mentorId` (Long, required): ID của mentor

**Example**: `/api/teams?CourseId=5&mentorId=10`

**Business Rules**:
- Mentor chỉ xem được teams của khóa học mà họ đang dạy
- Course phải tồn tại và thuộc về mentor

**Response**:
```json
{
  "success": true,
  "message": "string (auto-generated)",
  "data": [
    {
      "id": 1,
      "name": "Team Alpha",
      "courseId": 5,
      "courseName": "Software Development - SE1702",
      "courseCode": "SWD392_SE1702",
      "memberCount": 3,
      "mainIdeaId": 5,
      "mainIdeaName": "Student Management System",
      "members": [...],
      "ideas": [...]
    },
    {
      "id": 2,
      "name": "Team Beta",
      "courseId": 5,
      "courseName": "Software Development - SE1702",
      "courseCode": "SWD392_SE1702",
      "memberCount": 4,
      "mainIdeaId": 8,
      "mainIdeaName": "E-commerce Platform",
      "members": [...],
      "ideas": [...]
    }
  ]
}
```

---

### 6. Chọn idea chính cho team
**PUT** `/api/teams/{teamId}/select-idea`

**Mô tả**: Team leader chọn một idea làm idea chính của team từ các ideas của members

**Yêu cầu**: STUDENT role (phải là leader)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team

**Query Parameters**:
- `leaderEnrollmentId` (Long, required): ID enrollment của leader
- `ideaId` (Long, required): ID của idea muốn chọn

**Example**: `/api/teams/1/select-idea?leaderEnrollmentId=1&ideaId=5`

**Business Rules**:
- Chỉ leader mới có quyền chọn idea chính
- Idea phải thuộc về một member trong team
- Một team chỉ có một idea chính tại một thời điểm

**Response**:
```json
{
  "success": true,
  "message": "Chọn idea chính thành công",
  "data": {
    "id": 1,
    "name": "Team Alpha",
    "courseId": 5,
    "courseName": "Software Development - SE1702",
    "courseCode": "SWD392_SE1702",
    "semesterId": 2,
    "semesterName": "Fall Semester 2024",
    "mentorId": 10,
    "mentorName": "Nguyễn Văn Thầy",
    "memberCount": 3,
    "mainIdeaId": 5,
    "mainIdeaName": "Student Management System",
    "members": [...],
    "ideas": [...]
  }
}
```

---

### 7. Cập nhật tên team
**PUT** `/api/teams/{teamId}`

**Mô tả**: Team leader cập nhật tên team

**Yêu cầu**: STUDENT role (phải là leader)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team

**Query Parameters**:
- `leaderEnrollmentId` (Long, required): ID enrollment của leader
- `teamName` (String, required): Tên mới của team

**Example**: `/api/teams/1?leaderEnrollmentId=1&teamName=Team Alpha V2`

**Business Rules**:
- Chỉ leader mới có quyền đổi tên team
- Team name không được để trống

**Response**:
```json
{
  "success": true,
  "message": "Cập nhật tên team thành công",
  "data": {
    "id": 1,
    "name": "Team Alpha V2",
    "courseId": 5,
    "courseName": "Software Development - SE1702",
    "courseCode": "SWD392_SE1702",
    "semesterId": 2,
    "semesterName": "Fall Semester 2024",
    "mentorId": 10,
    "mentorName": "Nguyễn Văn Thầy",
    "memberCount": 3,
    "mainIdeaId": 5,
    "mainIdeaName": "Student Management System",
    "members": [...],
    "ideas": [...]
  }
}
```

---

### 8. Kick member khỏi team
**DELETE** `/api/teams/{teamId}/members/{enrollmentId}`

**Mô tả**: Team leader xóa một member khỏi team

**Yêu cầu**: STUDENT role (phải là leader)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team
- `enrollmentId` (Long): ID enrollment của member cần kick

**Query Parameters**:
- `leaderEnrollmentId` (Long, required): ID enrollment của leader

**Example**: `/api/teams/1/members/8?leaderEnrollmentId=1`

**Business Rules**:
- Chỉ leader mới có quyền kick members
- Leader không thể kick chính mình (phải dùng giải tán team)
- Member phải thuộc team này

**Response**:
```json
{
  "success": true,
  "message": "Xóa thành viên thành công"
}
```

---

### 9. Rời khỏi team
**POST** `/api/teams/{teamId}/leave`

**Mô tả**: Member rời khỏi team (không áp dụng cho leader)

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team

**Query Parameters**:
- `enrollmentId` (Long, required): ID enrollment của user muốn rời

**Example**: `/api/teams/1/leave?enrollmentId=8`

**Business Rules**:
- Member phải thuộc team này
- Leader không thể rời team (phải giải tán team hoặc chuyển quyền leader)
- Sau khi rời, member có thể tham gia team khác

**Response**:
```json
{
  "success": true,
  "message": "Rời khỏi team thành công"
}
```

---

### 10. Giải tán team
**DELETE** `/api/teams/{teamId}`

**Mô tả**: Team leader giải tán team (xóa team và tất cả members)

**Yêu cầu**: STUDENT role (phải là leader)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team

**Query Parameters**:
- `leaderEnrollmentId` (Long, required): ID enrollment của leader

**Example**: `/api/teams/1?leaderEnrollmentId=1`

**Business Rules**:
- Chỉ leader mới có quyền giải tán team
- Khi giải tán, tất cả members sẽ bị xóa khỏi team
- Sau khi giải tán, các members có thể tham gia team khác

**Response**:
```json
{
  "success": true,
  "message": "Giải tán team thành công"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Team name không được để trống"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Bạn không phải leader của team này"
}
```

```json
{
  "success": false,
  "message": "Leader không thể rời team, hãy giải tán team hoặc chuyển quyền leader"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Team không tồn tại"
}
```

```json
{
  "success": false,
  "message": "Enrollment không tồn tại"
}
```

```json
{
  "success": false,
  "message": "Thành viên không thuộc team này"
}
```

### 409 Conflict
```json
{
  "success": false,
  "message": "Bạn đã tham gia một team khác trong khóa học này"
}
```

```json
{
  "success": false,
  "message": "Không thể tự kick chính mình, hãy dùng chức năng giải tán team"
}
```

```json
{
  "success": false,
  "message": "Idea này không thuộc team của bạn"
}
```

---

## Complete Workflow Example

### Scenario: Tạo team và quản lý team hoàn chỉnh

#### Step 1: Sinh viên A tạo team
```http
POST /api/teams/create?enrollmentId=1&teamName=Team Alpha
Authorization: Bearer <student_a_token>

Response:
{
  "success": true,
  "message": "Tạo team thành công",
  "data": {
    "id": 1,
    "name": "Team Alpha",
    "memberCount": 1,
    "members": [
      {"userId": 3, "userFullName": "Sinh viên A", "isLeader": true}
    ]
  }
}
```

#### Step 2: Sinh viên B và C tham gia team (qua API invite hoặc join - not documented here)
```
Team hiện có 3 members: A (leader), B, C
```

#### Step 3: Các members tạo ideas
```http
POST /api/ideas/create?enrollmentId=1&name=Student Management&description=...
POST /api/ideas/create?enrollmentId=8&name=E-commerce&description=...
POST /api/ideas/create?enrollmentId=15&name=Learning Platform&description=...
```

#### Step 4: Leader chọn idea chính
```http
PUT /api/teams/1/select-idea?leaderEnrollmentId=1&ideaId=5
Authorization: Bearer <student_a_token>

Response:
{
  "success": true,
  "message": "Chọn idea chính thành công",
  "data": {
    "mainIdeaId": 5,
    "mainIdeaName": "Student Management"
  }
}
```

#### Step 5: Leader đổi tên team
```http
PUT /api/teams/1?leaderEnrollmentId=1&teamName=Team Alpha V2
Authorization: Bearer <student_a_token>

Response:
{
  "success": true,
  "message": "Cập nhật tên team thành công"
}
```

#### Step 6: Sinh viên C rời team
```http
POST /api/teams/1/leave?enrollmentId=15
Authorization: Bearer <student_c_token>

Response:
{
  "success": true,
  "message": "Rời khỏi team thành công"
}
```

#### Step 7: Leader kick sinh viên B
```http
DELETE /api/teams/1/members/8?leaderEnrollmentId=1
Authorization: Bearer <student_a_token>

Response:
{
  "success": true,
  "message": "Xóa thành viên thành công"
}
```

#### Step 8: Leader giải tán team
```http
DELETE /api/teams/1?leaderEnrollmentId=1
Authorization: Bearer <student_a_token>

Response:
{
  "success": true,
  "message": "Giải tán team thành công"
}
```

---

## Notes
- Một enrollment chỉ có thể tham gia 1 team trong cùng một khóa học
- Leader có toàn quyền quản lý team: đổi tên, kick members, chọn idea, giải tán
- Members có quyền: xem thông tin team, tạo ideas, rời team
- Khi giải tán team, tất cả members và dữ liệu liên quan sẽ bị xóa
- Ideas của members vẫn tồn tại ngay cả khi team bị giải tán
- Mentor có thể xem tất cả teams trong khóa học của mình
- Team phải có ít nhất 1 member (leader)
- Leader không thể rời team, chỉ có thể giải tán team