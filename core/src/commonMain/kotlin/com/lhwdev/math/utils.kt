package com.lhwdev.math


tailrec fun gcd(p: Int, q: Int): Int = if(q == 0) p else gcd(q, p % q)
tailrec fun gcd(p: Long, q: Long): Long = if(q == 0L) p else gcd(q, p % q)
