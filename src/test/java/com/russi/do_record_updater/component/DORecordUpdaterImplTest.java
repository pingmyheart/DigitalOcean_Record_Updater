package com.russi.do_record_updater.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DORecordUpdaterImplTest {

    @Autowired
    DORecordUpdaterImpl doRecordUpdater;

    @Test
    void testHasNext(){
        doRecordUpdater.getAllDomains();
    }

}