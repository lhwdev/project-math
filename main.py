

'''
사용 방법
`Term(계수, 문자, 지수)`: 항
`Fraction(분자, 분모)`: 분수
`Limit(문자, 다가가는 값, 계산값)`: 극한

예시:
Limit(
    symbol='x',
    approachTo=0,
    value=Fraction(
        numerator=Polynomial(terms=[Term(5, 'x', 2), Term(3, 'x', 1)]),
        denominator=Polynomial(terms=[Term(2, 'x', 1)])
    )
)

-> x가 0으로 다가갈 때 (5x^2 + 3x) / 2x의 극한값

출력값 읽기
a / b: 나눗셈(분수)
ax^n: a 곱하기 x의 n승

'''

# def main():
#     expr = Limit(
#         symbol='x',
#         approachTo=0,
#         value=Fraction(
#             numerator=Polynomial(terms=[Term(5, 'x', 2), Term(3, 'x', 1)]),
#             denominator=Polynomial(terms=[Term(2, 'x', 1)])
#         )
#     )
#     print(expr.latex())
#     print(expr)
#     print(expr.evaluate())




class Limit: # 극한
    def __init__(self, symbol, approachTo, value):
        self.symbol = symbol # 기호
        self.approachTo = approachTo # 다가가는 값
        self.value = value # lim 안의 값

    def __repr__(self): # __repr__이란 건 그냥 디버깅할 때 보기 좋게 할려고 넣는거임
        return f'lim_ {self.symbol}->{self.approachTo} ({self.value})'

    def evaluate(self): # evaluate는 계산하는 함수

        # 1. 0/0 꼴인지 확인
        # value가 분수라고 가정
        numValue = self.value.numerator.evaluate({ self.symbol: self.approachTo })
        denomValue = self.value.denominator.evaluate({ self.symbol: self.approachTo })

        if numValue == 0 and denomValue == 0:
            div = Polynomial([Term(1, self.symbol, 1), Term(-self.approachTo, self.symbol, 0)])
            return Limit(self.symbol, self.approachTo, Fraction(self.value.numerator.divideTo(div)[0], self.value.denominator.divideTo(div)[0])).evaluate()

        else:
            return self.value.evaluate({ self.symbol: self.approachTo })

    def latex(self):
        return f'\\lim_{{{self.symbol} \\to {self.approachTo}}} {self.value.latex()}'


class Polynomial: # 다항식
    def __init__(self, terms):
        self.terms = sorted(terms, key=termExponentKey, reverse=True) # 항들

    def __repr__(self):
        text = ''
        for index in range(0, len(self.terms)):
            term = self.terms[index]
            if index != 0: text += ' + '
            text += str(term)
        return text

    def evaluate(self, symbols):
        value = 0
        for term in self.terms:
            value += term.evaluate(symbols[term.symbol])
        return value

    def add(self, other):
        terms = self.terms + other.terms
        return Polynomial(reduceTerms(terms))

    def sub(self, other):
        return self.add(other.unaryMinus())

    def unaryMinus(self):
        return Polynomial([
            Term(coefficient=-term.coefficient, symbol=term.symbol, exponent=term.exponent) for term in self.terms
        ])

    def multiply(self, other):
        result = []
        for term in self.terms:
            for term2 in other.terms:
                result.append(term.multiply(term2))
        return Polynomial(reduceTerms(result))

    def multiplyWithTerm(self, other):
        result = [
            term.multiply(other) for term in self.terms
        ]
        return Polynomial(reduceTerms(result))

    def divideTo(self, other):
        state = self # 나눠야 할 것
        quotient = [] # 몫

        while True:
            if len(state.terms) == 0 or state.terms[0].exponent < other.terms[0].exponent: #
                break

            mul = state.terms[0].divideTo(other.terms[0])
            quotient.append(mul)
            numberToSub = other.multiplyWithTerm(mul)
            state = state.sub(numberToSub)

        return (Polynomial(quotient), state)

    def latex(self):
        return '+'.join([term.latex() for term in self.terms])



def termExponentKey(item):
    return item.exponent

def reduceTerms(terms): # 동류항 정리
    l = {}

    # 1. 문자 + 지수를 기준으로 묶기
    for term in terms:
        key = (term.symbol, term.exponent)
        if key in l:
            l[key] += term.coefficient
        else:
            l[key] = term.coefficient

    toRemove = []

    for key, value in l.items():
        # 2. 계수가 0인 항 제거
        if value == 0: toRemove.append(key)

    l2 = {}

    for key, value in l.items():
        if key not in toRemove: l2[key] = value

    return [
        Term(coefficient=value, symbol=key[0], exponent=key[1]) for key, value in l2.items()
    ]



# 5 x ^2
class Term: # 항
    def __init__(self, coefficient, symbol, exponent):
        self.coefficient = coefficient # 계수
        self.symbol = symbol # 기호
        self.exponent = exponent # 지수

    def __repr__(self):
        if self.exponent == 0:
            return f'{self.coefficient}'
        return f'{self.coefficient}{self.symbol}^{self.exponent}'

    def evaluate(self, symbolValue):
        return self.coefficient * pow(symbolValue, self.exponent)

    def multiply(self, other):
        if self.symbol != other.symbol: raise 'Error'
        return Term(self.coefficient * other.coefficient, self.symbol, self.exponent + other.exponent)

    def divideTo(self, other):
        if self.symbol != other.symbol: raise 'Error'
        return Term(self.coefficient / other.coefficient, self.symbol, self.exponent - other.exponent)

    def latex(self):
        if self.exponent == 0:
            return f'{self.coefficient}'
        if self.exponent == 1:
            return f'{self.coefficient}{self.symbol}'

        return f'{self.coefficient}{self.symbol}^{self.exponent}'


class Fraction: # 분수
    def __init__(self, numerator, denominator):
        self.numerator = numerator # 분자
        self.denominator = denominator # 분모

    def __repr__(self):
        return f'({self.numerator}) / ({self.denominator})'

    def evaluate(self, symbols):
        return self.numerator.evaluate(symbols) / self.denominator.evaluate(symbols)

    def latex(self):
        return f'\\frac{{{self.numerator}}}{{{self.denominator}}}'

