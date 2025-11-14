# Idea Management APIs

Base URL: `/api/ideas`

## Overview
Idea Management APIs cho phép sinh viên tạo và quản lý các ý tưởng dự án trong khóa học. Mỗi idea thuộc về một enrollment và có thể được chọn làm idea chính cho team.

## Liên kết với Team APIs
- Mỗi member trong team có thể tạo nhiều ideas
- Leader có thể chọn 1 idea từ các ideas của members làm idea chính cho team
- Ideas vẫn tồn tại ngay cả khi member rời team hoặc team bị giải tán

---

## Endpoints

### 1. Tạo idea mới
**POST** `/api/ideas/create`

**Mô tả**: Sinh viên tạo idea mới cho một enrollment

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `enrollmentId` (Long, required): ID của enrollment
- `name` (String, required): Tên của idea
- `description` (String, required): Mô tả chi tiết của idea

**Example**: `/api/ideas/create?enrollmentId=1&name=Student Management System&description=Hệ thống quản lý sinh viên với đầy đủ tính năng`

**Business Rules**:
- Enrollment phải tồn tại và thuộc về user hiện tại
- Name và description không được để trống
- Một enrollment có thể tạo nhiều ideas

**Response**:
```json
{
  "success": true,
  "message": "Tạo idea thành công",
  "data": {
    "ideaId": 5,
    "name": "Student Management System",
    "description": "Hệ thống quản lý sinh viên với đầy đủ tính năng CRUD, authentication, phân quyền",
    "enrollment": {
      "enrollmentId": 1,
      "user": {
        "userId": 3,
        "fullName": "Nguyễn Văn A",
        "email": "student1@fpt.edu.vn"
      },
      "course": {
        "courseId": 5,
        "code": "SWD392_SE1702",
        "name": "Software Development - SE1702"
      }
    },
    "createdAt": "2024-11-14T10:30:00",
    "updatedAt": "2024-11-14T10:30:00"
  }
}
```

---

### 2. Xem chi tiết idea
**GET** `/api/ideas/{ideaId}`

**Mô tả**: Lấy thông tin chi tiết của một idea

**Yêu cầu**: Authenticated user

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `ideaId` (Long): ID của idea

**Example**: `/api/ideas/5`

**Response**:
```json
{
  "success": true,
  "message": "Lấy thông tin idea thành công",
  "data": {
    "ideaId": 5,
    "name": "Student Management System",
    "description": "Hệ thống quản lý sinh viên với đầy đủ tính năng CRUD, authentication, phân quyền",
    "enrollment": {
      "enrollmentId": 1,
      "user": {
        "userId": 3,
        "fullName": "Nguyễn Văn A",
        "email": "student1@fpt.edu.vn"
      },
      "course": {
        "courseId": 5,
        "code": "SWD392_SE1702",
        "name": "Software Development - SE1702"
      }
    },
    "createdAt": "2024-11-14T10:30:00",
    "updatedAt": "2024-11-14T10:30:00"
  }
}
```

---

### 3. Xem ideas của tôi
**GET** `/api/ideas/my-ideas`

**Mô tả**: Lấy danh sách tất cả ideas của một enrollment

**Yêu cầu**: STUDENT role

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Query Parameters**:
- `enrollmentId` (Long, required): ID của enrollment

**Example**: `/api/ideas/my-ideas?enrollmentId=1`

**Response**:
```json
{
  "success": true,
  "message": "string (auto-generated)",
  "data": [
    {
      "ideaId": 5,
      "name": "Student Management System",
      "description": "Hệ thống quản lý sinh viên với đầy đủ tính năng",
      "enrollment": {
        "enrollmentId": 1,
        "user": {
          "userId": 3,
          "fullName": "Nguyễn Văn A",
          "email": "student1@fpt.edu.vn"
        }
      },
      "createdAt": "2024-11-14T10:30:00",
      "updatedAt": "2024-11-14T10:30:00"
    },
    {
      "ideaId": 8,
      "name": "Online Exam System",
      "description": "Hệ thống thi trực tuyến",
      "enrollment": {
        "enrollmentId": 1,
        "user": {
          "userId": 3,
          "fullName": "Nguyễn Văn A",
          "email": "student1@fpt.edu.vn"
        }
      },
      "createdAt": "2024-11-13T14:20:00",
      "updatedAt": "2024-11-13T14:20:00"
    }
  ]
}
```

---

### 4. Xem tất cả ideas của team
**GET** `/api/ideas/team/{teamId}`

**Mô tả**: Lấy danh sách tất cả ideas từ các members của team

**Yêu cầu**: Authenticated user

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `teamId` (Long): ID của team

**Example**: `/api/ideas/team/1`

**Business Rules**:
- Chỉ lấy ideas của các members hiện tại trong team
- Bao gồm cả idea đã được chọn làm main idea

**Response**:
```json
{
  "success": true,
  "message": "Lấy danh sách idea của team thành công",
  "data": [
    {
      "ideaId": 5,
      "name": "Student Management System",
      "description": "Hệ thống quản lý sinh viên với đầy đủ tính năng",
      "enrollment": {
        "enrollmentId": 1,
        "user": {
          "userId": 3,
          "fullName": "Nguyễn Văn A",
          "email": "student1@fpt.edu.vn"
        }
      },
      "createdAt": "2024-11-14T10:30:00",
      "updatedAt": "2024-11-14T10:30:00"
    },
    {
      "ideaId": 7,
      "name": "Online Learning Platform",
      "description": "Nền tảng học trực tuyến với video calls",
      "enrollment": {
        "enrollmentId": 8,
        "user": {
          "userId": 12,
          "fullName": "Trần Thị B",
          "email": "student2@fpt.edu.vn"
        }
      },
      "createdAt": "2024-11-14T11:00:00",
      "updatedAt": "2024-11-14T11:00:00"
    },
    {
      "ideaId": 9,
      "name": "E-commerce Website",
      "description": "Website thương mại điện tử B2C",
      "enrollment": {
        "enrollmentId": 15,
        "user": {
          "userId": 20,
          "fullName": "Lê Văn C",
          "email": "student3@fpt.edu.vn"
        }
      },
      "createdAt": "2024-11-14T11:30:00",
      "updatedAt": "2024-11-14T11:30:00"
    }
  ]
}
```

---

### 5. Cập nhật idea
**PUT** `/api/ideas/{ideaId}`

**Mô tả**: Cập nhật thông tin của idea (chỉ owner)

**Yêu cầu**: STUDENT role (phải là owner của idea)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `ideaId` (Long): ID của idea

**Query Parameters**:
- `enrollmentId` (Long, required): ID enrollment của owner
- `name` (String, required): Tên mới của idea
- `description` (String, required): Mô tả mới của idea

**Example**: `/api/ideas/5?enrollmentId=1&name=Student Management System V2&description=Updated description`

**Business Rules**:
- Chỉ owner của idea mới có quyền cập nhật
- Name và description không được để trống
- Enrollment phải khớp với owner của idea

**Response**:
```json
{
  "success": true,
  "message": "Cập nhật idea thành công",
  "data": {
    "ideaId": 5,
    "name": "Student Management System V2",
    "description": "Hệ thống quản lý sinh viên với đầy đủ tính năng CRUD, authentication, phân quyền, reporting",
    "enrollment": {
      "enrollmentId": 1,
      "user": {
        "userId": 3,
        "fullName": "Nguyễn Văn A",
        "email": "student1@fpt.edu.vn"
      },
      "course": {
        "courseId": 5,
        "code": "SWD392_SE1702",
        "name": "Software Development - SE1702"
      }
    },
    "createdAt": "2024-11-14T10:30:00",
    "updatedAt": "2024-11-14T15:45:00"
  }
}
```

---

### 6. Xóa idea
**DELETE** `/api/ideas/{ideaId}`

**Mô tả**: Xóa idea (chỉ owner)

**Yêu cầu**: STUDENT role (phải là owner của idea)

**Headers**: 
```
Authorization: Bearer <access_token>
```

**Path Parameters**:
- `ideaId` (Long): ID của idea

**Query Parameters**:
- `enrollmentId` (Long, required): ID enrollment của owner

**Example**: `/api/ideas/5?enrollmentId=1`

**Business Rules**:
- Chỉ owner của idea mới có quyền xóa
- Không thể xóa idea đang được chọn làm main idea của team
- Enrollment phải khớp với owner của idea

**Response**:
```json
{
  "success": true,
  "message": "Xóa idea thành công"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Tên idea không được để trống"
}
```

```json
{
  "success": false,
  "message": "Mô tả không được để trống"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Bạn không có quyền cập nhật idea này"
}
```

```json
{
  "success": false,
  "message": "Bạn không có quyền xóa idea này"
}
```

```json
{
  "success": false,
  "message": "Không thể xóa idea đang được chọn làm main idea của team"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Idea không tồn tại"
}
```

```json
{
  "success": false,
  "message": "Enrollment không tồn tại"
}
```

---

## Integration with Team APIs

### Complete Workflow: Tạo ideas và chọn main idea cho team

#### Step 1: Members tạo ideas
```http
# Member A tạo idea
POST /api/ideas/create?enrollmentId=1&name=Student Management&description=...
Authorization: Bearer <member_a_token>

# Member B tạo idea
POST /api/ideas/create?enrollmentId=8&name=E-commerce&description=...
Authorization: Bearer <member_b_token>

# Member C tạo idea
POST /api/ideas/create?enrollmentId=15&name=Learning Platform&description=...
Authorization: Bearer <member_c_token>
```

#### Step 2: Xem tất cả ideas của team
```http
GET /api/ideas/team/1
Authorization: Bearer <any_member_token>

Response:
{
  "success": true,
  "data": [
    {"ideaId": 5, "name": "Student Management", "ownerId": 3},
    {"ideaId": 7, "name": "E-commerce", "ownerId": 12},
    {"ideaId": 9, "name": "Learning Platform", "ownerId": 20}
  ]
}
```

#### Step 3: Leader chọn main idea
```http
PUT /api/teams/1/select-idea?leaderEnrollmentId=1&ideaId=5
Authorization: Bearer <leader_token>

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

#### Step 4: Owner cập nhật idea
```http
PUT /api/ideas/5?enrollmentId=1&name=Student Management V2&description=...
Authorization: Bearer <member_a_token>

Response:
{
  "success": true,
  "message": "Cập nhật idea thành công"
}
```

#### Step 5: Member rời team (idea vẫn tồn tại)
```http
POST /api/teams/1/leave?enrollmentId=15
Authorization: Bearer <member_c_token>

# Idea của member C vẫn tồn tại trong database
# Nhưng sẽ không hiển thị trong danh sách ideas của team nữa
```

---

## Notes
- Một enrollment có thể tạo nhiều ideas
- Ideas thuộc về enrollment, không phải team
- Khi member rời team, ideas của họ vẫn tồn tại nhưng không còn liên kết với team
- Chỉ ideas của members hiện tại trong team mới xuất hiện khi get ideas by team
- Leader chỉ có thể chọn ideas của members hiện tại trong team làm main idea
- Owner có toàn quyền quản lý ideas của mình: tạo, sửa, xóa
- Không thể xóa idea đang được chọn làm main idea của team
- Ideas có thể được sử dụng để đánh giá, bình chọn trong tương lai