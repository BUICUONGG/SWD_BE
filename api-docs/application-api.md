# Application Management APIs

Base URL: `/api/applications`

## Overview
Application APIs quản lý việc apply vào team và invite sinh viên vào team. Đây là cách duy nhất để sinh viên tham gia vào team đã tồn tại (ngoài việc tự tạo team mới).

## Hai cách tham gia Team:
1. **Tạo team mới**: Sinh viên tự tạo team và trở thành leader
2. **Tham gia team có sẵn**:
   - **Apply**: Sinh viên gửi đơn xin vào team → Leader approve
   - **Invite**: Leader mời sinh viên vào team → Sinh viên accept

## Application Flow:

### Apply Flow (Sinh viên chủ động):
```
1. Sinh viên xem teams trong course
2. Sinh viên apply vào team → Application (status: PENDING)
3. Leader xem applications của team
4. Leader approve/reject → Application (status: APPROVED/REJECTED)
5. Nếu approved → Sinh viên tự động join team
```

### Invite Flow (Leader chủ động):
```
1. Leader xem danh sách enrollments trong course
2. Leader invite sinh viên → Application (status: PENDING)
3. Sinh viên xem invitations
4. Sinh viên accept/reject → Application (status: APPROVED/REJECTED)
5. Nếu accepted → Sinh viên tự động join team
```

## Application Types:
- **APPLY**: Sinh viên gửi đơn xin vào team (applicationType = APPLY)
- **INVITE**: Leader mời sinh viên vào team (applicationType = INVITE)

## Application Status:
- **PENDING**: Đang chờ xử lý
- **APPROVED**: Đã chấp nhận → Sinh viên join team
- **REJECTED**: Đã từ chối
- **CANCELLED**: Đã hủy bỏ

---

## Endpoints

### 1. Apply vào team
**POST** `/api/applications/apply`

**Mô tả**: Sinh viên gửi đơn xin tham gia team

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `enrollmentId` (Long, required): ID enrollment của sinh viên muốn apply
- `teamId` (Long, required): ID của team muốn tham gia

**Example**: `/api/applications/apply?enrollmentId=10&teamId=1`

**Business Rules**:
- Enrollment phải được APPROVED
- Enrollment chưa tham gia team nào trong cùng course
- Team phải thuộc cùng course với enrollment
- Không thể apply vào team của chính mình
- Không có application PENDING nào với team này

**Response**:
```json
{
  "success": true,
  "message": "Gửi đơn tham gia thành công",
  "data": {
    "applicationId": 1,
    "applicationType": "APPLY",
    "status": "PENDING",
    "enrollmentId": 10,
    "applicantUserId": 3,
    "applicantUserName": "Nguyễn Văn A",
    "applicantEmail": "student1@fpt.edu.vn",
    "teamId": 1,
    "teamName": "Team Alpha",
    "courseId": 1,
    "courseName": "Software Development - SE1702",
    "createdAt": "2024-11-14T10:30:00",
    "handledAt": null,
    "handledBy": null
  }
}
```

**Use Case**: Sinh viên A đã enroll course SWD392, xem team "Team Alpha" và muốn tham gia

---

### 2. Mời sinh viên vào team
**POST** `/api/applications/invite`

**Mô tả**: Leader mời sinh viên khác vào team của mình

**Yêu cầu**: STUDENT role (phải là leader)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `leaderEnrollmentId` (Long, required): ID enrollment của leader
- `targetEnrollmentId` (Long, required): ID enrollment của sinh viên muốn mời

**Example**: `/api/applications/invite?leaderEnrollmentId=8&targetEnrollmentId=10`

**Business Rules**:
- Leader phải có team (là leader của một team)
- Target enrollment phải được APPROVED
- Target enrollment chưa tham gia team nào trong cùng course
- Target enrollment phải cùng course với team của leader
- Không có invitation PENDING nào với target này

**Response**:
```json
{
  "success": true,
  "message": "Gửi lời mời thành công",
  "data": {
    "applicationId": 2,
    "applicationType": "INVITE",
    "status": "PENDING",
    "enrollmentId": 10,
    "applicantUserId": 3,
    "applicantUserName": "Nguyễn Văn A",
    "applicantEmail": "student1@fpt.edu.vn",
    "teamId": 1,
    "teamName": "Team Alpha",
    "courseId": 1,
    "courseName": "Software Development - SE1702",
    "invitedBy": 12,
    "invitedByName": "Trần Thị B",
    "createdAt": "2024-11-14T11:00:00",
    "handledAt": null,
    "handledBy": null
  }
}
```

**Use Case**: Leader của Team Alpha muốn mời sinh viên A tham gia team

---

### 3. Xử lý application (Accept/Reject)
**PUT** `/api/applications/{applicationId}/handle`

**Mô tả**: 
- Nếu là APPLY: Leader xử lý đơn (approve/reject)
- Nếu là INVITE: Sinh viên xử lý lời mời (accept/reject)

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `applicationId` (Long): ID của application

**Query Parameters**:
- `leaderEnrollmentId` (Long, required): ID enrollment của người xử lý
- `accepted` (boolean, required): true = chấp nhận, false = từ chối

**Example**: 
- Leader approve: `/api/applications/1/handle?leaderEnrollmentId=8&accepted=true`
- Sinh viên accept invite: `/api/applications/2/handle?leaderEnrollmentId=10&accepted=true`

**Business Rules**:
- Application phải ở status PENDING
- Nếu APPLY: Chỉ leader của team mới có quyền xử lý
- Nếu INVITE: Chỉ sinh viên được mời mới có quyền xử lý
- Nếu accepted = true:
  - Application status → APPROVED
  - Sinh viên tự động join team
  - Các applications khác của sinh viên này → CANCELLED
- Nếu accepted = false:
  - Application status → REJECTED

**Response (accepted = true)**:
```json
{
  "success": true,
  "message": "Xử lý đơn thành công",
  "data": {
    "applicationId": 1,
    "applicationType": "APPLY",
    "status": "APPROVED",
    "enrollmentId": 10,
    "applicantUserId": 3,
    "applicantUserName": "Nguyễn Văn A",
    "teamId": 1,
    "teamName": "Team Alpha",
    "handledAt": "2024-11-14T12:00:00",
    "handledBy": 12,
    "handledByName": "Trần Thị B"
  }
}
```

**Response (accepted = false)**:
```json
{
  "success": true,
  "message": "Xử lý đơn thành công",
  "data": {
    "applicationId": 1,
    "applicationType": "APPLY",
    "status": "REJECTED",
    "enrollmentId": 10,
    "applicantUserId": 3,
    "applicantUserName": "Nguyễn Văn A",
    "teamId": 1,
    "teamName": "Team Alpha",
    "handledAt": "2024-11-14T12:00:00",
    "handledBy": 12,
    "handledByName": "Trần Thị B"
  }
}
```

---

### 4. Xem applications của tôi
**GET** `/api/applications/my-applications`

**Mô tả**: Xem tất cả applications (sent và received) của enrollment

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `enrollmentId` (Long, required): ID enrollment

**Example**: `/api/applications/my-applications?enrollmentId=10`

**Response**:
```json
{
  "success": true,
  "message": "Lấy danh sách đơn thành công",
  "data": [
    {
      "applicationId": 1,
      "applicationType": "APPLY",
      "status": "PENDING",
      "enrollmentId": 10,
      "applicantUserName": "Nguyễn Văn A",
      "teamId": 1,
      "teamName": "Team Alpha",
      "createdAt": "2024-11-14T10:30:00"
    },
    {
      "applicationId": 2,
      "applicationType": "INVITE",
      "status": "PENDING",
      "enrollmentId": 10,
      "applicantUserName": "Nguyễn Văn A",
      "teamId": 2,
      "teamName": "Team Beta",
      "invitedByName": "Lê Văn C",
      "createdAt": "2024-11-14T11:00:00"
    },
    {
      "applicationId": 3,
      "applicationType": "APPLY",
      "status": "REJECTED",
      "enrollmentId": 10,
      "teamId": 3,
      "teamName": "Team Gamma",
      "createdAt": "2024-11-13T15:00:00",
      "handledAt": "2024-11-13T16:00:00"
    }
  ]
}
```

**Use Case**: Sinh viên xem:
- Các đơn apply mà mình đã gửi (APPLY)
- Các lời mời mà mình nhận được (INVITE)

---

### 5. Xem applications của team (Leader only)
**GET** `/api/applications/team/{teamId}`

**Mô tả**: Leader xem tất cả applications gửi đến team của mình

**Yêu cầu**: STUDENT role (phải là leader)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team

**Query Parameters**:
- `leaderEnrollmentId` (Long, required): ID enrollment của leader

**Example**: `/api/applications/team/1?leaderEnrollmentId=8`

**Business Rules**:
- Chỉ leader của team mới xem được
- Hiển thị cả APPLY và INVITE của team

**Response**:
```json
{
  "success": true,
  "message": "Lấy danh sách đơn của team thành công",
  "data": [
    {
      "applicationId": 1,
      "applicationType": "APPLY",
      "status": "PENDING",
      "enrollmentId": 10,
      "applicantUserId": 3,
      "applicantUserName": "Nguyễn Văn A",
      "applicantEmail": "student1@fpt.edu.vn",
      "applicantMajor": "Software Engineering",
      "teamId": 1,
      "teamName": "Team Alpha",
      "createdAt": "2024-11-14T10:30:00"
    },
    {
      "applicationId": 4,
      "applicationType": "APPLY",
      "status": "PENDING",
      "enrollmentId": 15,
      "applicantUserId": 5,
      "applicantUserName": "Phạm Văn D",
      "applicantEmail": "student3@fpt.edu.vn",
      "applicantMajor": "Artificial Intelligence",
      "teamId": 1,
      "teamName": "Team Alpha",
      "createdAt": "2024-11-14T11:30:00"
    }
  ]
}
```

**Use Case**: Leader xem danh sách sinh viên đã apply vào team để approve/reject

---

### 6. Hủy application
**DELETE** `/api/applications/{applicationId}`

**Mô tả**: Hủy application đang PENDING

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `applicationId` (Long): ID của application

**Query Parameters**:
- `enrollmentId` (Long, required): ID enrollment của người hủy

**Example**: `/api/applications/1?enrollmentId=10`

**Business Rules**:
- Chỉ có thể hủy application PENDING
- Nếu APPLY: Chỉ applicant mới hủy được
- Nếu INVITE: Cả leader và invitee đều hủy được
- Application status → CANCELLED

**Response**:
```json
{
  "success": true,
  "message": "Hủy đơn thành công"
}
```

**Use Case**: 
- Sinh viên hủy đơn apply đã gửi
- Leader hủy lời mời đã gửi
- Sinh viên từ chối lời mời (có thể dùng handle với accepted=false)

---

## Complete Workflows

### Workflow 1: Sinh viên Apply vào Team

```http
# Step 1: Sinh viên xem teams trong course
GET /api/teams?CourseId=1&mentorId=5
Authorization: Bearer <student_a_token>

Response: List teams including Team Alpha (teamId=1)

# Step 2: Sinh viên apply vào Team Alpha
POST /api/applications/apply?enrollmentId=10&teamId=1
Authorization: Bearer <student_a_token>

Response:
{
  "applicationId": 1,
  "applicationType": "APPLY",
  "status": "PENDING"
}

# Step 3: Leader xem applications của team
GET /api/applications/team/1?leaderEnrollmentId=8
Authorization: Bearer <leader_token>

Response: List applications including applicationId=1

# Step 4: Leader approve application
PUT /api/applications/1/handle?leaderEnrollmentId=8&accepted=true
Authorization: Bearer <leader_token>

Response:
{
  "applicationId": 1,
  "status": "APPROVED"
}

# Step 5: Kiểm tra team details (sinh viên đã join)
GET /api/teams/1
Authorization: Bearer <any_token>

Response:
{
  "teamId": 1,
  "members": [
    {"enrollmentId": 8, "isLeader": true},
    {"enrollmentId": 10, "isLeader": false}  // Sinh viên A đã join
  ]
}
```

### Workflow 2: Leader Invite Sinh viên vào Team

```http
# Step 1: Leader xem enrollments trong course
GET /api/enrollments/course/1
Authorization: Bearer <leader_token>

Response: List enrollments including enrollmentId=10 (Sinh viên A)

# Step 2: Leader mời sinh viên A
POST /api/applications/invite?leaderEnrollmentId=8&targetEnrollmentId=10
Authorization: Bearer <leader_token>

Response:
{
  "applicationId": 2,
  "applicationType": "INVITE",
  "status": "PENDING"
}

# Step 3: Sinh viên A xem invitations
GET /api/applications/my-applications?enrollmentId=10
Authorization: Bearer <student_a_token>

Response: List applications including applicationId=2 (INVITE)

# Step 4: Sinh viên A accept invitation
PUT /api/applications/2/handle?leaderEnrollmentId=10&accepted=true
Authorization: Bearer <student_a_token>

Response:
{
  "applicationId": 2,
  "status": "APPROVED"
}

# Step 5: Kiểm tra team details (sinh viên đã join)
GET /api/teams/1

Response: Sinh viên A đã là member của team
```

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Enrollment chưa được approve"
}
```

```json
{
  "success": false,
  "message": "Bạn đã tham gia một team khác trong khóa học này"
}
```

```json
{
  "success": false,
  "message": "Đã có đơn pending với team này"
}
```

```json
{
  "success": false,
  "message": "Team và enrollment không cùng course"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Chỉ leader mới có quyền xử lý đơn apply"
}
```

```json
{
  "success": false,
  "message": "Chỉ người được mời mới có quyền xử lý invitation"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Application không tồn tại"
}
```

```json
{
  "success": false,
  "message": "Team không tồn tại"
}
```

### 409 Conflict
```json
{
  "success": false,
  "message": "Application này đã được xử lý"
}
```

```json
{
  "success": false,
  "message": "Không thể apply vào team của chính mình"
}
```

---

## Notes
- **Application là cầu nối** giữa Enrollment và Team
- Có 2 loại applications: APPLY (sinh viên chủ động) và INVITE (leader chủ động)
- Khi application được APPROVED, sinh viên tự động join team
- Một enrollment chỉ có thể join 1 team trong cùng course
- Khi join team, tất cả applications PENDING khác của enrollment sẽ tự động CANCELLED
- Application PENDING có thể bị hủy bởi người gửi hoặc người nhận
- Application APPROVED/REJECTED không thể thay đổi
- Leader có thể mời nhiều sinh viên cùng lúc (gửi nhiều invitations)
- Sinh viên có thể apply vào nhiều teams, nhưng chỉ được join 1 team