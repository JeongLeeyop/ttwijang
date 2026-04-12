export const discountValue = (value: number, rate: number) => value - (value * (rate / 100));

export const discountRest = (value: number, rate: number) => value * (rate / 100);
