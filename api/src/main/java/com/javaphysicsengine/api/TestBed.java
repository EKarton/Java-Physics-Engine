package com.javaphysicsengine.api;

import org.ejml.data.*;
import org.ejml.dense.fixed.CommonOps_DDF2;
import org.ejml.dense.fixed.CommonOps_DDF3;
import org.ejml.dense.fixed.NormOps_DDF2;
import org.ejml.dense.row.CommonOps_CDRM;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.ops.CommonOps_BDRM;
import org.ejml.simple.SimpleMatrix;

public class TestBed {
    public static void main(String[] args) {
        DMatrix2 vector1 = new DMatrix2(1, 2);
        DMatrix2 vector2 = new DMatrix2(3, 4);

        vector1.print();
        vector2.print();

        // Adding two vectors
        DMatrix2 vector3 = new DMatrix2();
        CommonOps_DDF2.add(vector1, vector2, vector3);
        vector3.print();

        // Subtracting two vectors
        DMatrix2 vector6 = new DMatrix2();
        CommonOps_DDF2.subtract(vector1, vector2, vector6);
        vector6.print();

        // Dot product
        double dot = CommonOps_DDF2.dot(vector1, vector2);
        System.out.println(dot);

        // Element-wise Multiply
        DMatrix2 vector4 = new DMatrix2();
        CommonOps_DDF2.elementMult(vector1, vector2, vector4);
        vector4.print();

        // Min

        // Max

        // Normalize
        NormOps_DDF2.normalizeF(vector1);
        vector1.print();

        // Get the norm
        System.out.println(NormOps_DDF2.normF(vector1));

        // Get the norm2
        SimpleMatrix a = SimpleMatrix.wrap(new DMatrixRMaj(1, 2));
        System.out.println(a.normF());

        // Multiply by scalar
        DMatrix2 vector5 = new DMatrix2();
        CommonOps_DDF2.scale(2, vector1, vector5);
        vector5.print();
    }
}
