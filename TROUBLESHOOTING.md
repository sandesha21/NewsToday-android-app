# Troubleshooting Guide - Local News App

## Issue: "No Internet Connection" Screen on App Launch

### Symptoms:
- App shows error message: "It appears that either your device is not connected to the internet. OR Our server is currently down."
- No news articles are displayed
- Blank white screen with error icon

---

## Possible Causes & Solutions

### 1. **Emulator/Device Internet Connection**

#### Check if emulator has internet:
```bash
# In Android Studio, open the emulator and check:
# Settings > About phone > Status
# Look for "Mobile network state" or "Wi-Fi"
```

#### Solutions:
- **For Emulator**: 
  - Ensure your host machine has internet
  - Restart the emulator
  - Go to emulator settings and check network configuration
  - Try: `adb shell ping 8.8.8.8`

- **For Physical Device**:
  - Enable Wi-Fi or mobile data
  - Check if internet is working (open browser)
  - Ensure app has internet permission in AndroidManifest.xml

---

### 2. **API Key Configuration**

The app uses **The Guardian API** which requires a valid API key.

#### Check API Key Setup:
1. Open `gradle.properties` file
2. Verify these lines exist:
   ```properties
   guard_a=494a
   guard_b=364b5d0b
   guard_c=99cb
   guard_d=4ba5
   guard_e=e4364203b869
   ```

#### Get a New API Key:
1. Visit: https://open-platform.theguardian.com/access/
2. Click "Register developer key" (for non-commercial use)
3. Fill in the registration form
4. You'll receive your API key
5. Update `gradle.properties` with your key parts

#### How the API Key is Built:
The app combines 5 parts in this order: `GUARD_B-GUARD_A-GUARD_D-GUARD_C-GUARD_E`

Example: If your API key is `abc123def456`, split it and add to gradle.properties

---

### 3. **The Guardian API Status**

#### Check if API is working:
```bash
# Replace YOUR_API_KEY with your actual key
curl "https://content.guardianapis.com/search?api-key=YOUR_API_KEY&format=json"
```

If you get a JSON response, the API is working.

---

### 4. **App Permissions**

Ensure the app has internet permission in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

These should already be present in your app.

---

## Debugging Steps

### Step 1: Check Logcat
1. Open Android Studio
2. Go to: View > Tool Windows > Logcat
3. Run the app and look for error messages
4. Search for "QueryUtils" or "NewsActivity" in logs

### Step 2: Test Network Connectivity
```bash
# In Android Studio terminal:
adb shell ping 8.8.8.8
```

### Step 3: Verify API Key
1. Rebuild the project: `Build > Clean Project` then `Build > Rebuild Project`
2. Run the app again

### Step 4: Check Settings
1. Open app Settings (gear icon)
2. Verify a category is selected
3. Verify number of articles is set (1-200)

---

## Common Issues & Fixes

| Issue | Solution |
|-------|----------|
| Emulator shows "no internet" | Restart emulator, check host internet |
| API returns 401 error | API key is invalid or expired, get a new one |
| API returns 400 error | Check URL parameters in NewsActivity.java |
| App crashes on launch | Check Logcat for exceptions |
| Blank screen with no error | Check if layout files exist in res/layout/ |

---

## Quick Checklist

- [ ] Emulator/Device has internet connection
- [ ] API key is valid and configured in gradle.properties
- [ ] Project is rebuilt after changing gradle.properties
- [ ] App has INTERNET permission in AndroidManifest.xml
- [ ] The Guardian API is accessible (test with curl)
- [ ] Settings have valid category and article count

---

## Still Having Issues?

1. **Check Logcat** for detailed error messages
2. **Verify API key** by testing directly: https://open.theguardian.com/documentation/
3. **Rebuild project** completely: `Build > Clean Project` + `Build > Rebuild Project`
4. **Restart emulator** or reconnect physical device
5. **Check internet** on your host machine

---

## Useful Links

- **The Guardian Open Platform**: https://open-platform.theguardian.com/access/
- **Android Permissions**: https://developer.android.com/guide/topics/permissions/overview
- **Android Networking**: https://developer.android.com/training/basics/network-ops
