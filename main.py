# ---------------------------------------------- #
#                  < IMPORTS >
# ---------------------------------------------- #

from math import cos, exp
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import scipy.stats as sp
import scipy.interpolate as spi
import math
import matplotlib.figure as fig
from scipy import stats


# ---------------------------------------------- #
#             < VARIABLES GLOBALES >
# ---------------------------------------------- #

k = 10**4
gamma = 100
m = 70
dt = 1e-3
simDuration = 5

forceCalculator = lambda r,v: - (k * r) - (gamma * v)

# ---------------------------------------------- #
#                  < PARTICULA >
# ---------------------------------------------- #

class Particle:
    def __init__(self, mass, x, y, vx, vy):
        self.mass = mass
        self.x = x
        self.y = y
        self.vx = vx
        self.vy = vy

# ---------------------------------------------- #
#            < ALGORITMO DE EULER >
# ---------------------------------------------- #

class Euler:

    def __init__(self, particle, timeStep):
        self.particle = particle
        self.timeStep = timeStep
    
    def run(self):
        # Calculamos la fuerza ... 
        force = forceCalculator(self.particle.x, self.particle.vx)

        # Calculamos la velocidad ...
        newVelocity = self.particle.vx + (self.timeStep / m) * force

        # Calculamos la posicion ...
        newPosition = self.particle.x + (self.timeStep * newVelocity) + (self.timeStep**2) * force / (2 * m)
        return Particle(m, newPosition, 0, newVelocity, 0)

# ---------------------------------------------- #
#            < ALGORITMO DE BEEMAN >
# ---------------------------------------------- #

class Beeman:
    def __init__(self, particle, prevPartticle, timeStep):
        self.particle = particle
        self.prevParticle = prevPartticle
        self.timeStep = timeStep

    def run(self):

        # Primero creamos lo que duraria la simulacion
        r = np.arange(0, simDuration, self.timeStep)
        v = np.arange(0, simDuration, self.timeStep)

        # Seteamos las condiciones iniciales...
        r[0] = self.particle.x
        v[0] = self.particle.vx

        # Comenzamos el algoritmo ...
        next = 1 # la siguiente posicion que queremos
        step = 0 # paso en el que estamos
        time = step * self.timeStep
        first = True

        prevX = self.prevParticle.x
        prevV = self.prevParticle.vx

        while ( time < (simDuration - self.timeStep) ):

            if(first):
                # Pos
                r[next] = r[step] + (v[step] * self.timeStep) + (2/3) * (forceCalculator(r[step],v[step])/m) * (self.timeStep**2) - (1/6) * (forceCalculator(prevX, prevV)/m) * (self.timeStep**2)
                # Pred
                predV = v[step] + (3/2) * (forceCalculator(r[step], v[step])/m) * self.timeStep - 0.5 * (forceCalculator(prevX, prevV)/m) * self.timeStep
                # Correc
                v[next] = v[step] + (1/3) * (forceCalculator(r[next], predV)/m) * self.timeStep + (5/6) * (forceCalculator(r[step], v[step])/m) * self.timeStep - (1/6) * (forceCalculator(prevX, prevV)/m) * self.timeStep
                first = False
            else:
                # Pos
                r[next] = r[step] + v[step] * self.timeStep + (2/3) * (forceCalculator(r[step],v[step])/m) * (self.timeStep**2) - (1/6) * (forceCalculator(r[step-1], v[[step-1]])/m) * (self.timeStep**2)
                # Pred
                predV = v[step] + (3/2) * (forceCalculator(r[step], v[step])/m) * self.timeStep - 0.5 * (forceCalculator(r[step-1], v[step-1])/m) * self.timeStep
                # Correc
                v[next] = v[step] + (1/3) * (forceCalculator(r[next], predV)/m) * self.timeStep + (5/6) * (forceCalculator(r[step], v[step])/m) * self.timeStep - (1/6) * (forceCalculator(r[step-1], v[step-1])/m) * self.timeStep
            next += 1
            step += 1
            time = step * self.timeStep

        return r, v

# ---------------------------------------------- #
#            < ALGORITMO VERLET >
# ---------------------------------------------- #

class Verlet:
    def __init__(self, particle, prevParticle, timeStep):
        self.particle = particle
        self.prevParticle = prevParticle
        self.timeStep = timeStep
    
    def run(self):

        # Primero creamos lo que duraria la simulacion
        r = np.arange(0, simDuration, self.timeStep)
        v = np.arange(0, simDuration, self.timeStep)

        # Seteamos las condiciones iniciales
        r[0] = self.particle.x
        v[0] = self.particle.vx

        next = 1
        step = 0
        time = step * self.timeStep
        first = True

        prevX = self.prevParticle.x

        while( time < simDuration - self.timeStep):

            if(first):
                # Calculamos la posicion ...
                r[next] = (2 * r[step]) -  prevX + ((self.timeStep**2)/m) * forceCalculator(r[step], v[step])
                # Calculamos la velocidad usando la pos calculada arriba
                v[next] = (r[next]-prevX)/(2*self.timeStep)
                first = False
            else:
                # Calculamos la posicion ...
                r[next] = (2 * r[step]) -  r[step-1] + ((self.timeStep**2)/m) * forceCalculator(r[step], v[step])
                # Calculamos la velocidad usando la pos calculada arriba
                v[next] = (r[next]-r[step-1])/(2*self.timeStep)
            
            next += 1
            step += 1
            time = step * self.timeStep
        
        return r, v

# ---------------------------------------------- #
#              < ALGORITMO GEAR >
# ---------------------------------------------- #

class GPC:
    def __init__(self, particle, timeStep):
        self.particle = particle
        self.timeStep = timeStep
    
    def run(self):

        # Creamos los lugares donde vamos a guardar la pos y la velocidad
        r = np.arange(0, simDuration, self.timeStep)
        r1 = np.arange(0, simDuration, self.timeStep)

        # Inicializamos
        r[0] = self.particle.x
        r1[0] = self.particle.vx

        # Coeficientes que hay que usar
        coefs = [3/16, 251/360, 1, 11/18, 1/6, 1/60]

        # Derivadas
        r2 = lambda t : -k/m * r[int(t/self.timeStep)] - (gamma/m) * r1[int(t/self.timeStep)]
        r3 = lambda t : -k/m * r1[int(t/self.timeStep)] - (gamma/m) * r2(t)
        r4 = lambda t : -k/m * r2(t) - (gamma/m) * r3(t)
        r5 = lambda t : -k/m * r3(t) - (gamma/m) * r4(t)

        next = 1 # Paso siguiente
        step = 0 # Donde estamos
        time = step * self.timeStep

        while( time < simDuration - self.timeStep ):

            # Predecimos
            pred_r  =  r[step] + (r1[step] * self.timeStep) + r2(time) * ((self.timeStep**2)/2) + r3(time) * ((self.timeStep**3)/6) + r4(time) * ((self.timeStep**4)/24) + r5(time) * ((self.timeStep**5)/120)
            pred_r1 = r1[step] + (r2(time) * self.timeStep) + r3(time) * ((self.timeStep**2)/2) + r4(time) * ((self.timeStep**3)/6) + r5(time) * ((self.timeStep**4)/24)
            pred_r2 = r2(time) + (r3(time) * self.timeStep) + r4(time) * ((self.timeStep**2)/2) + r5(time) * ((self.timeStep**3)/6)

            # Evaluamos
            acc = forceCalculator(pred_r, pred_r1)/m
            DR2 = ((acc - pred_r2) * (self.timeStep**2)) / 2

            # Corregimos errores
            r[next] = pred_r + coefs[0] * DR2 
            r1[next] = pred_r1 + coefs[1] * DR2 * (1/self.timeStep)

            next += 1
            step += 1
            time = step*self.timeStep
        return r, r1

# ---------------------------------------------- #
#            < FUNCION ANALITICA >
# ---------------------------------------------- #

class Analytic:
    def __init__(self, timStep):
        self.timeStep = timStep
    
    def run(self):
        # Creamos todas las posiciones ...
        r = np.arange(0, simDuration, self.timeStep)

        # Iteramos
        i = 0
        while( i < len(r) ):
            termE = exp(-(gamma/(2*m))*i*self.timeStep)
            inCos = (k/m) - ((gamma**2)/(4*(m**2)))
            termCos = cos((inCos**0.5)*i*self.timeStep)
            r[i] = termE*termCos
            i += 1
        return r

def ejercicio1P2(iParticle):

    inDts = [ 1e-3, 1e-4, 1e-5, 1e-6]
    
    ecm_v = []
    ecm_b = []
    ecm_gpc = []
    
    for inDt in inDts:
        euler = Euler(iParticle, -inDt)
        prev = euler.run()

        beeman = Beeman(iParticle, prev, inDt)
        r_b, v_b = beeman.run()

        verlet = Verlet(iParticle, prev, inDt)
        r_v,v_v = verlet.run()

        gpc = GPC(iParticle, inDt)
        r_g, v_g = gpc.run()

        analytic = Analytic(inDt)
        r_a = analytic.run()

        ecm_b.append(ECM(r_b, r_a))
        ecm_v.append(ECM(r_v, r_a))
        ecm_gpc.append(ECM(r_g, r_a))
    
    parameters = {'xtick.labelsize': 12,'ytick.labelsize': 12, 'axes.labelsize': 14, 'legend.fontsize': 13}
    plt.rcParams.update(parameters)

    plt.figure(figsize=(7,5))
    plt.semilogy()
    plt.semilogx()
    plt.scatter(inDts, ecm_v)
    plt.plot(inDts, ecm_v, label = "Vertel")
    plt.scatter(inDts, ecm_b)
    plt.plot(inDts, ecm_b, label = "Beeman")
    plt.scatter(inDts, ecm_gpc)
    plt.plot(inDts, ecm_gpc, label = "Gear Predictor-Corrector")
    plt.xlabel("Dt [s]")
    plt.ylabel("ECM")
    plt.legend()
    plt.show()
    return ecm_b, ecm_v, ecm_gpc

def ECM(x, a):
    dif = []
    i = 0
    for p in x:
        dif.append((p-a[i])**2)
        i+=1
    s = sum(dif)

    return s/len(x)

def graph(x, a, t):
    parameters = {'xtick.labelsize': 12,'ytick.labelsize': 12, 'axes.labelsize': 14, 'legend.fontsize': 13}
    plt.rcParams.update(parameters)

    plt.figure(figsize=(7,5))

    plt.plot(t, x, label = "Gear Predictor-Corrector")
    plt.plot(t, a, label = "Analítico", linestyle = "-.")
    plt.xlabel("Tiempo [s]")
    plt.ylabel("X [m]")
    plt.legend()
    plt.show()
# Main
initial = Particle(m, 1, 0, -gamma/(2*m), 0)

# b, v, gpc = ejercicio1P2(initial)

# print("Beeman : " + str(b))
# print("Verlet : " + str(v))
# print("GPC : " + str(gpc))


# gpc = GPC(initial, 1e-3)
# r, v = gpc.run()
# analytic = Analytic(1e-3)
# ra = analytic.run()
# print(ECM(r, ra))
dt = 1e-3
euler = Euler(initial, -dt)
prev = euler.run()
beeman = Beeman(initial, prev, dt)
r_b, v_b = beeman.run()
verlet = Verlet(initial, prev, dt)
r_v,v_v = verlet.run()
gpc = GPC(initial, dt)
r_g, v_g = gpc.run()
analytic = Analytic(dt)
ra = analytic.run()
t = []
j = 0
for i in range(0,int(5/dt)):
    t.append(i * dt)

parameters = {'xtick.labelsize': 12,'ytick.labelsize': 12, 'axes.labelsize': 14, 'legend.fontsize': 13}
plt.rcParams.update(parameters)

plt.figure(figsize=(7,5))
plt.plot(t, r_v, label = "Verlet", linestyle = "-.")
plt.plot(t, r_b, label = "Beeman", linestyle = "-.")
plt.plot(t, r_g, label = "Gear Predictor-Corrector", linestyle = "-.")
plt.plot(t, ra, label = "Analítico", linestyle = "-.")
plt.xlabel("Tiempo [s]")
plt.ylabel("X [m]")
plt.legend()
plt.show()



