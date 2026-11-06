# 💻 Remote Controller for Windows

Control your **Windows PC remotely** from your Android device.  
This app allows you to shut down, reboot, sleep, lock, or wake your PC over a local network — all in a clean and modern UI.

<table>
  <tr>
    <th>Device Control Screen</th>
    <th>Add Device Screen</th>
    <th>İnfo Pop Up</th>
  </tr>

  <tr>
    <td><img src="https://github.com/user-attachments/assets/8c769699-2d3a-432d-8e24-dbad4d6ed508" height="500"></td>
    <td><img src="https://github.com/user-attachments/assets/5d1f207a-98a5-4024-add3-cca2ed90427d" height="500"></td>
     <td><img src="https://github.com/user-attachments/assets/3081361e-d1d3-4b50-87cb-6f9a229c27e7" height="500"></td>
  </tr>
</table>

---

## 🚀 Features

- ✅ Remotely **shut down** your Windows PC  
- 🔁 Remotely **reboot** your PC  
- 🌙 Put your PC into **sleep mode**  
- 🔒 **Lock** your PC with one tap  
- ⚡ **Wake-on-LAN** support to **turn on** your PC remotely  
- 📡 View IP address, MAC address, ping time, and device details  
- 💾 Local storage of devices using **Jetpack DataStore**  
- 📱 Built with **Jetpack Compose** and **Material 3**

---

## 📦 Tech Stack

- **Kotlin** & **Jetpack Compose**
- **Material 3** design system
- **DataStore Preferences** for local storage
- **Retrofit** for networking 
- **MVVM architecture**

---
## 📚 Libraries Used

- 🎨 **Jetpack Compose UI**
  - Compose UI
  - Graphics
  - Tooling Preview

- 🧱 **Material 3**
  - Material Design 3 components

- 🔄 **Lifecycle & ViewModel**
  - Lifecycle Runtime
  - ViewModel

- 📲 **Activity & Navigation**
  - Activity Compose
  - Navigation Compose

- 💾 **Local Storage**
  - Jetpack DataStore (Preferences)

- 🌐 **Networking**
  - Retrofit
  - Gson Converter

- 🧪 **Testing & Debug**
  - JUnit
  - Espresso
  - Compose UI Test
  - Tooling & Manifest Debug Tools


## 🖥️ Server-side Setup (Python)

This Android app communicates with a Python script running on your Windows PC. The script listens for commands and executes system-level actions like shutdown, restart, etc.

### 🔧 Requirements

- Python 3.x  
- Flask (or built-in HTTP server)  
- Admin privileges for actions like shutdown/lock  

### 📜 Example Flask Server (Python)

```python
from flask import Flask, request
import os

app = Flask(__name__)

@app.route('/shutdown', methods=['POST'])
def shutdown():
    os.system("shutdown /s /t 1")
    return 'Shutting down', 200

@app.route('/restart', methods=['POST'])
def restart():
    os.system("shutdown /r /t 1")
    return 'Rebooting', 200

@app.route('/sleep', methods=['POST'])
def sleep():
    os.system("rundll32.exe powrprof.dll,SetSuspendState 0,1,0")
    return 'Sleeping', 200

@app.route('/lock', methods=['POST'])
def lock():
    os.system("rundll32.exe user32.dll,LockWorkStation")
    return 'Locked', 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
```

> 🔒 Make sure your firewall allows incoming requests on the port you choose.

You can host this script on your local network. The Android app will send HTTP POST requests to endpoints like `/shutdown`, `/restart`, etc.


