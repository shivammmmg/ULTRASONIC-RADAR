% ============================================================
% Ultrasonic Radar System (Major Project)
% Author: Shivam Gupta
% Course: EECS 1011 - York University
%
% Description:
% MATLAB–Arduino radar system using ultrasonic sensor and servo motor.
% Scans 180°, measures distance, and plots real-time radar output.
%
% Note:
% It may include redundant or incomplete sections.
%
% ============================================================

clear all;

figure('units','normalized','outerposition',[0 0 1 1]);
whitebg('black');
ax = polaraxes;
ax.ThetaZeroLocation = 'top';
ax.ThetaDir = 'clockwise';
ax.ThetaLim = [0, 360];
ax.RLim = [0, 200];
ax.ThetaTick = [0,45,90,135,180,225,270,315];
ax.ThetaTickLabel = {'0','45','90','135','180','225','270','315'};
ax.RTick = [0,50,100,150,200];
bg_color = [0 0 0];
line_color = [0.603922 , 0.803922 , 0.196078];
scatter_color = [0.854902 , 0.647059 , 0.12549];
set(gcf,'color',bg_color);
th = linspace(0,2*pi,1000);
R = 10:10:200;

for i=1:length(R)
    x = R(i)*cos(th);
    y = R(i)*sin(th);
    polarplot(ax, th, R(i)*ones(size(th)),'Color', line_color,'LineWidth',1);
    hold on
end

x0 = [0 50*cosd(45) 100*cosd(90) 50*cosd(135) 0 50*cosd(225) 100*cosd(270) 50*cosd(315)];
y0 = [0 50*sind(45) 100*sind(90) 50*sind(135) 0 50*sind(225) 100*sind(270) 50*sind(315)];

for i=1:length(x0)
    hold on;
    polarplot(ax, [0 atan2d(y0(i),x0(i))], [0 norm([x0(i),y0(i)])],'Color', line_color,'LineWidth',2);
end

a = arduino('/dev/cu.usbmodem101', 'Uno', 'Libraries', {'Ultrasonic', 'Servo'});

servoPin = 'D2';
echoPin = 'D6';
triggerPin = 'D7';
greenLedPin = 'D8';
redLedPin = 'D9';

servo_motor = servo(a, servoPin);
disp('Servo motor initialized');
sensor = ultrasonic(a, triggerPin, echoPin);

figure;
ax = polaraxes;
ax.ThetaZeroLocation = 'top';
ax.ThetaDir = 'clockwise';
ax.ThetaLim = [0, 180];
ax.RLim = [0, 200];

table = zeros(180, 2);

threshold_dist = 20; % cm

while true
    closeby = false;
    for theta = 0:1:179
        writePosition(servo_motor, theta/180);
        dist1 = readDistance(sensor);
        pause(.04);
        dist2 = readDistance(sensor);
        dist = (dist1 + dist2) / 2;

        if dist < 20
            closeby = true;
        end

        table(theta + 1, 1) = theta;
        table(theta + 1, 2) = round(dist * 100, 2);
        polarplot(ax, table(:,1) * pi/180, table(:,2));
        title(ax, 'Map of the Environment');
        drawnow limitrate;
    end

    for theta = 179:-1:0
        writePosition(servo_motor, theta/180);
        dist1 = readDistance(sensor);
        pause(.04);
        dist2 = readDistance(sensor);
        dist = (dist1 + dist2) / 2;

        if dist < 20
            closeby = true;
        end

        table(theta + 1, 1) = theta;
        table(theta + 1, 2) = round(dist * 100, 2);
        polarplot(ax, table(:,1) * pi/180, table(:,2));
        title(ax, 'Map of the Environment');
        drawnow limitrate;
    end

    if closeby
        analogWrite(a, greenLedPin, 255);
        analogWrite(a, redLedPin, 0);
    else
        analogWrite(a, redLedPin, 128);
        analogWrite(a, greenLedPin, 0);
    end
end
