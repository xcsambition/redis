package com.zfx.command.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EchoCommandTest {

    @Mock
    private Random random;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("测试前准备");

    }

    @Test
    void execute() {

        Mockito.when(random.nextInt()).thenReturn(100);
        Assertions.assertEquals(100,random.nextInt());
//        Mockito.verify(random,Mockito.times(2)).nextInt();
    }
    @AfterEach
    void after() {
        System.out.println("结束了");
    }


}