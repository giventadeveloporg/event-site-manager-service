# Product Requirements Document (PRD)
## Email Subscription Management - Frontend Implementation

### **Problem Statement**
Users are experiencing "Invalid or expired token" errors when attempting to unsubscribe from emails through the frontend application. The current implementation has several issues:

1. **Token Mismatch**: The unsubscribe endpoint expects a JWT token, but the frontend may be sending a different type of token
2. **Missing Token Generation**: The frontend doesn't generate proper JWT tokens for unsubscribe operations
3. **Inconsistent Token Handling**: Different endpoints use different token generation strategies
4. **Poor Error Handling**: Users receive generic error messages without clear guidance

### **Current Backend Behavior Analysis**

#### **Token Generation Patterns:**
1. **User Registration**: Generates JWT token and stores in `emailSubscriptionToken` field
2. **Email Sending**: Uses stored `emailSubscriptionToken` from database
3. **Unsubscribe**: Expects JWT token that matches the email subject
4. **Resubscribe**: Generates new JWT token

#### **Backend Endpoints:**
- `GET /api/unsubscribe-email?email={email}&token={jwt_token}` - Unsubscribe
- `GET /api/resubscribe-email?email={email}&tenantId={tenantId}` - Resubscribe (no token needed)

### **Root Cause Analysis**
The frontend is likely:
1. Not generating proper JWT tokens for unsubscribe operations
2. Using stored tokens that may be expired or invalid
3. Not handling the token validation flow correctly
4. Missing proper error handling for different failure scenarios

---

## **Solution Requirements**

### **1. Frontend Token Management**

#### **1.1 JWT Token Generation**
```typescript
// Required: Implement JWT token generation for unsubscribe operations
interface UnsubscribeTokenRequest {
  email: string;
  tenantId: string;
}

interface UnsubscribeTokenResponse {
  success: boolean;
  token?: string;
  message?: string;
}
```

#### **1.2 Token Storage Strategy**
- Store JWT tokens securely (localStorage/sessionStorage)
- Implement token refresh mechanism
- Handle token expiration gracefully

### **2. User Profile Form Enhancement**

#### **2.1 Email Subscription Toggle**
```typescript
interface EmailSubscriptionForm {
  email: string;
  tenantId: string;
  isEmailSubscribed: boolean;
  currentToken?: string;
}
```

#### **2.2 Required Form Fields**
- Email address (read-only)
- Tenant ID (hidden field)
- Subscription status toggle
- Current subscription token (hidden)

### **3. API Integration**

#### **3.1 New Backend Endpoint (Required)**
```typescript
// Add this endpoint to backend
POST /api/user-profiles/generate-unsubscribe-token
{
  "email": "user@example.com",
  "tenantId": "tenant_123"
}
Response: {
  "success": true,
  "token": "jwt_token_here",
  "message": "Token generated successfully"
}
```

#### **3.2 Frontend API Calls**
```typescript
// 1. Generate unsubscribe token
const generateUnsubscribeToken = async (email: string, tenantId: string) => {
  const response = await fetch('/api/user-profiles/generate-unsubscribe-token', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, tenantId })
  });
  return response.json();
};

// 2. Unsubscribe with token
const unsubscribeEmail = async (email: string, token: string) => {
  const response = await fetch(`/api/unsubscribe-email?email=${email}&token=${token}`);
  return response.json();
};

// 3. Resubscribe
const resubscribeEmail = async (email: string, tenantId: string) => {
  const response = await fetch(`/api/resubscribe-email?email=${email}&tenantId=${tenantId}`);
  return response.json();
};
```

### **4. User Experience Flow**

#### **4.1 Subscription Toggle Behavior**
1. **User clicks unsubscribe toggle**
2. **Frontend generates new JWT token** (if not available)
3. **Frontend calls unsubscribe API** with generated token
4. **Display success/error message** based on response
5. **Update UI state** accordingly

#### **4.2 Error Handling**
```typescript
interface UnsubscribeError {
  type: 'TOKEN_EXPIRED' | 'INVALID_TOKEN' | 'EMAIL_NOT_FOUND' | 'ALREADY_UNSUBSCRIBED' | 'NETWORK_ERROR';
  message: string;
  retryable: boolean;
}

const handleUnsubscribeError = (error: UnsubscribeError) => {
  switch (error.type) {
    case 'TOKEN_EXPIRED':
      // Regenerate token and retry
      return regenerateTokenAndRetry();
    case 'INVALID_TOKEN':
      // Generate new token
      return generateNewToken();
    case 'EMAIL_NOT_FOUND':
      // Show error message
      return showError('Email not found in our system');
    case 'ALREADY_UNSUBSCRIBED':
      // Update UI to show already unsubscribed
      return updateUIState('unsubscribed');
    case 'NETWORK_ERROR':
      // Show retry option
      return showRetryOption();
  }
};
```

### **5. UI/UX Requirements**

#### **5.1 Subscription Status Display**
```jsx
const EmailSubscriptionToggle = ({ userProfile, onToggle }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleToggle = async (newStatus) => {
    setIsLoading(true);
    setError(null);

    try {
      if (newStatus === false) {
        // Unsubscribe flow
        const tokenResponse = await generateUnsubscribeToken(userProfile.email, userProfile.tenantId);
        if (tokenResponse.success) {
          const unsubscribeResponse = await unsubscribeEmail(userProfile.email, tokenResponse.token);
          if (unsubscribeResponse.success) {
            onToggle(false);
          } else {
            setError(unsubscribeResponse.message);
          }
        } else {
          setError('Failed to generate unsubscribe token');
        }
      } else {
        // Resubscribe flow
        const resubscribeResponse = await resubscribeEmail(userProfile.email, userProfile.tenantId);
        if (resubscribeResponse.success) {
          onToggle(true);
        } else {
          setError(resubscribeResponse.message);
        }
      }
    } catch (err) {
      setError('An unexpected error occurred');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="email-subscription-toggle">
      <label>
        <input
          type="checkbox"
          checked={userProfile.isEmailSubscribed}
          onChange={(e) => handleToggle(e.target.checked)}
          disabled={isLoading}
        />
        Receive email notifications
      </label>
      {isLoading && <span>Processing...</span>}
      {error && <div className="error">{error}</div>}
    </div>
  );
};
```

#### **5.2 Error Messages**
- **"Invalid or expired token"** → "Please try again. We'll generate a new verification link."
- **"Email not found"** → "This email is not registered in our system."
- **"Already unsubscribed"** → "You're already unsubscribed from emails."
- **"Network error"** → "Connection failed. Please check your internet and try again."

### **6. Backend Changes Required**

#### **6.1 Add Token Generation Endpoint**
```java
@PostMapping("/user-profiles/generate-unsubscribe-token")
public ResponseEntity<Map<String, Object>> generateUnsubscribeToken(
    @RequestBody Map<String, String> request) {
    String email = request.get("email");
    String tenantId = request.get("tenantId");

    // Generate JWT token
    JwtClaimsSet claims = JwtClaimsSet.builder().subject(email).build();
    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
    String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    // Update user profile with new token
    Optional<UserProfileDTO> userOpt = userProfileService.findByEmailAndTenantId(email, tenantId);
    if (userOpt.isPresent()) {
        UserProfileDTO user = userOpt.get();
        user.setEmailSubscriptionToken(token);
        userProfileService.save(user);
    }

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("token", token);
    response.put("message", "Token generated successfully");
    return ResponseEntity.ok(response);
}
```

### **7. Testing Requirements**

#### **7.1 Test Cases**
1. **Happy Path**: User successfully unsubscribes with valid token
2. **Token Expiry**: Handle expired token gracefully
3. **Invalid Token**: Handle malformed token
4. **Network Error**: Handle connection failures
5. **Already Unsubscribed**: Handle duplicate unsubscribe attempts
6. **Email Not Found**: Handle non-existent email addresses

#### **7.2 Test Data**
```typescript
const testCases = [
  {
    name: "Valid unsubscribe",
    email: "test@example.com",
    tenantId: "tenant_123",
    expectedResult: "success"
  },
  {
    name: "Expired token",
    email: "test@example.com",
    token: "expired_token",
    expectedResult: "token_expired"
  },
  {
    name: "Invalid email",
    email: "nonexistent@example.com",
    tenantId: "tenant_123",
    expectedResult: "email_not_found"
  }
];
```

### **8. Implementation Priority**

#### **Phase 1 (Critical)**
1. Add token generation endpoint to backend
2. Implement frontend token generation
3. Fix unsubscribe flow with proper error handling
4. Add user-friendly error messages

#### **Phase 2 (Enhancement)**
1. Add retry mechanism for failed requests
2. Implement token refresh functionality
3. Add loading states and better UX
4. Add comprehensive error logging

#### **Phase 3 (Optimization)**
1. Add offline support
2. Implement token caching
3. Add analytics for subscription changes
4. Add email confirmation for subscription changes

### **9. Success Metrics**
- **Error Rate**: < 5% of unsubscribe attempts fail
- **User Experience**: Clear error messages for all failure scenarios
- **Performance**: < 2 seconds for unsubscribe operations
- **Reliability**: 99% success rate for valid requests

---

## **Technical Implementation Notes**

### **JWT Token Structure**
```json
{
  "sub": "user@example.com",
  "iat": 1640995200,
  "exp": 1640998800
}
```

### **Error Response Format**
```json
{
  "success": false,
  "message": "Human-readable error message",
  "errorCode": "TOKEN_EXPIRED",
  "retryable": true
}
```

### **Security Considerations**
1. **Token Expiration**: Set reasonable expiration times (1 hour)
2. **Rate Limiting**: Implement rate limiting for token generation
3. **Validation**: Validate email format and tenant ID
4. **Logging**: Log all subscription changes for audit purposes

---

## **Quick Implementation Checklist**

### **Backend Tasks**
- [ ] Add `POST /api/user-profiles/generate-unsubscribe-token` endpoint
- [ ] Update error responses to include error codes
- [ ] Add proper logging for subscription changes
- [ ] Test token generation and validation

### **Frontend Tasks**
- [ ] Implement token generation API call
- [ ] Update unsubscribe flow to use generated tokens
- [ ] Add proper error handling for all scenarios
- [ ] Implement user-friendly error messages
- [ ] Add loading states and retry mechanisms
- [ ] Test all error scenarios

### **Testing Tasks**
- [ ] Test valid unsubscribe flow
- [ ] Test expired token handling
- [ ] Test invalid token handling
- [ ] Test network error scenarios
- [ ] Test already unsubscribed scenario
- [ ] Test email not found scenario

This PRD provides a comprehensive solution to fix the unsubscribe token validation issue while improving the overall user experience for email subscription management.
