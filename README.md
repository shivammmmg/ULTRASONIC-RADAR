# 🛰️ Ultrasonic Radar System (Arduino + MATLAB)

An **object detection and ranging system** that uses an **HC-SR04 ultrasonic sensor** and **TowerPro SG90 servo motor** with **Arduino UNO** to simulate a short-range RADAR.  
The system scans the environment in a 180° arc, detects obstacles, calculates their distance using the **speed of sound**, and visualizes the data in **MATLAB**.

---

🎯 **Objective**  
To design and implement an **ultrasonic RADAR** that can detect nearby obstacles and display their distance and position using **MATLAB** polar visualization — demonstrating real-time sensing, servo control, and signal processing.

---

## 🧩 Features
- 📡 Real-time object detection using ultrasonic waves  
- ⚙️ Servo motor scanning across 0–180°  
- 💻 MATLAB-based radar visualization (polar plot)  
- 🚦 LED indicators for proximity alerts  
- 🔁 Automatic back-and-forth scanning motion  
- 🧠 Distance calculation based on echo time-of-flight  

---

## ⚙️ Hardware Components
| Component | Description |
|------------|-------------|
| **Arduino UNO R3** | Core microcontroller |
| **HC-SR04 Ultrasonic Sensor** | Emits and detects ultrasonic waves |
| **TowerPro SG90 Servo Motor** | Rotates the sensor for scanning |
| **Green & Red LEDs** | Indicate proximity of objects |
| **100Ω Resistors** | Used for LED current limiting |
| **Jumper Wires & Breadboard** | Circuit connections |
| **Ultrasonic Sensor Holder** | Holds the HC-SR04 module securely |

---

## 🧰 Hardware Setup
<p align="center">
  <img src="images/setup.jpg" width="600">
  <br>
  <em>Figure 1: Full hardware setup showing Arduino, servo, and ultrasonic sensor.</em>
</p>

---

## 💻 Software Overview
**Tools Used**
- **MATLAB** (for visualization and control)
- **Arduino IDE** (for firmware)
- **Ultrasonic & Servo Libraries** (from MathWorks)
- **Arduino Support Package for MATLAB**

**Key File**
| File | Description |
|------|--------------|
| `/code/ultrasonic_radar.m` | MATLAB script for radar scanning, distance measurement, and real-time plotting |

---

## ⚡ Working Principle
The system emits ultrasonic pulses via the **HC-SR04 sensor**.  
When these pulses hit an obstacle, the reflected echo is captured by the receiver.  
The time delay is used to compute the distance using the formula:

\[
\text{Distance} = \frac{Speed\ of\ Sound \times Echo\ Time}{2}
\]

The servo motor sweeps the sensor from 0° to 180° and back, generating a polar radar map in MATLAB.

---

## 📈 Mathematical Model
| Actual Distance (cm) | Measured (cm) | Error |
|----------------------|---------------|-------|
| 100 | 100.1 | ±0.1 |
| 200 | 200.0 | ±0.2 |
| 250 | 250.1 | ±0.1 |

---

## 📊 MATLAB Visualization
<p align="center">
  <img src="images/radar_output.png" width="600">
  <br>
  <em>Figure 2: Real-time radar visualization in MATLAB.</em>
</p>

---

## 🧠 Code Availability
The MATLAB script used for radar visualization and distance measurement is available in the `/code` folder.

> ⚠️ Note: The code file was retrieved from my original eClass submission for EECS 1011.
> It represents the version I submitted as part of my major project and may include redundant or incomplete lines from the coursework template.

---

## 🧪 Testing & Results
- System successfully detected obstacles within ~3 m range.  
- Distance measurements were accurate within ±10 cm.  
- MATLAB visualization updated in real time.  
- LEDs switched between **green (safe)** and **red (object detected)**.  

---

## 🚀 Future Enhancements
- Expand range and resolution with multiple sensors.  
- Add **GUI-based radar visualization** in MATLAB.  
- Integrate **IoT connectivity** for cloud monitoring.  
- Implement **object tracking** using angle–distance correlation.

---

## 🎓 Learning Outcomes
- Gained experience in **MATLAB–Arduino integration**.  
- Improved understanding of **signal timing and ultrasonic sensing**.  
- Learned **servo motor control and data plotting**.  
- Enhanced documentation and testing skills.

---

## 🧑‍💻 Author
**Shivam Gupta**  
🎓 B.Eng. Software Engineering @ York University  
📧 inbox11shivam@gmail.com  
🔗 [LinkedIn](https://linkedin.com/in/shivammmmg) • [Portfolio](https://shivammmmg.com)

---

## 📚 References
- [Arduino UNO Documentation](https://docs.arduino.cc/hardware/uno-rev3)  
- [HC-SR04 Ultrasonic Sensor Guide](https://components101.com/sensors/hc-sr04-ultrasonic-sensor)  
- [TowerPro SG90 Servo Datasheet](https://www.towerpro.com.sg/product/sg90-9g-micro-servo/)  
- [MATLAB Arduino I/O Library](https://www.mathworks.com/help/supportpkg/arduinoio/)


[Major_Project_Report_.docx](https://github.com/Shivammmmg/ULTRASONIC-RADAR/files/15446263/Major_Project_Report_.docx)
