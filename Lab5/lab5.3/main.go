package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func init() {
	rand.Seed(time.Now().Unix())
}

type Barrier struct {
	closed, workerSize int
	mutex              sync.Mutex
	start, end         chan int
}

func NewBarrier(size int) *Barrier {
	elem := Barrier{workerSize: size, start: make(chan int, 1), end: make(chan int, 1)}
	elem.end <- 1
	return &elem
}
func (barrier *Barrier) bStart() {
	barrier.mutex.Lock()
	barrier.closed += 1
	if barrier.closed == barrier.workerSize {
		<-barrier.end
		barrier.start <- 1
	}
	barrier.mutex.Unlock()
	<-barrier.start
	barrier.start <- 1
}
func (barrier *Barrier) bEnd() {
	barrier.mutex.Lock()
	barrier.closed -= 1
	if barrier.closed == 0 {
		<-barrier.start
		barrier.end <- 1
	}
	barrier.mutex.Unlock()
	<-barrier.end
	barrier.end <- 1
}

type NumArr struct {
	sync.Mutex
	arr1, arr2, arr3 []int
	space            int
	stopExecutingApp bool
}

func NewNumArr(parr1, parr2, parr3 []int) *NumArr {
	elem := NumArr{arr1: parr1, arr2: parr2, arr3: parr3, space: 0}
	return &elem
}
func (arr *NumArr) work(option int) {
	arr.Lock()
	op := rand.Intn(2)
	if option == 1 {
		if op == 0 {
			arr.arr1[rand.Intn(len(arr.arr1))] += 1
		} else if op == 1 {
			arr.arr1[rand.Intn(len(arr.arr1))] -= 1
		}
	} else if option == 2 {
		if op == 0 {
			arr.arr2[rand.Intn(len(arr.arr2))] += 1
		} else if op == 1 {
			arr.arr2[rand.Intn(len(arr.arr2))] -= 1
		}
	} else {
		if op == 0 {
			arr.arr3[rand.Intn(len(arr.arr3))] += 1
		} else if op == 1 {
			arr.arr3[rand.Intn(len(arr.arr3))] -= 1
		}
	}
	arr.Unlock()
}
func (arr *NumArr) printConcreteArr(option int, addOpt bool) {
	arr.Lock()
	if addOpt {
		fmt.Print("arr", option, " проводить маніпуляції.. Результат: ")
	} else {
		fmt.Print("arr", option, ". Елементи : ")
	}
	if option == 1 {
		for i := 0; i < len(arr.arr1); i++ {
			fmt.Print(arr.arr1[i], " ")
		}
		fmt.Println("")
	} else if option == 2 {
		for i := 0; i < len(arr.arr2); i++ {
			fmt.Print(arr.arr2[i], " ")
		}
		fmt.Println("")
	} else {
		for i := 0; i < len(arr.arr3); i++ {
			fmt.Print(arr.arr3[i], " ")
		}
		fmt.Println("")
	}
	arr.Unlock()
}
func (arr *NumArr) stopGoroutine() bool {
	arr.Lock()
	sum1 := 0
	sum2 := 0
	sum3 := 0
	for i := 0; i < len(arr.arr1); i++ {
		sum1 += arr.arr1[i]
		sum2 += arr.arr2[i]
		sum3 += arr.arr3[i]
	}
	if sum1 == sum2 && sum1 == sum3 {
		arr.Unlock()
		return true
	} else {
		arr.Unlock()
		return false
	}
}
func (arr *NumArr) consoleSpacing() {
	arr.Lock()
	arr.space++
	if arr.space%3 == 0 {
		fmt.Println("")
		arr.space = 0
	}
	arr.Unlock()
}
func (arr *NumArr) stopExecuting() bool {
	return arr.stopExecutingApp
}

func worker(br *Barrier, arr *NumArr, option int) {
	for {
		br.bStart()
		if arr.stopGoroutine() {
			fmt.Print("arr", option, " побачив що всі масиви рівні і припинив роботу\n")
			arr.stopExecutingApp = true
			br.bEnd()
			break
		} else {
			arr.work(option)
			arr.printConcreteArr(option, true)
			arr.consoleSpacing()
			br.bEnd()
		}
	}
}

func main() {
	arr := NewNumArr([]int{2}, []int{1}, []int{1})
	br := NewBarrier(3)
	fmt.Println("-----------------------------------------------------------")
	fmt.Println("Початковий стан:")
	arr.printConcreteArr(1, false)
	arr.printConcreteArr(2, false)
	arr.printConcreteArr(3, false)
	fmt.Println("-----------------------------------------------------------")
	fmt.Println("Процес:")
	go worker(br, arr, 1)
	go worker(br, arr, 2)
	go worker(br, arr, 3)

	for {
		if arr.stopExecuting() {
			break
		}
	}
}
