export const discountValue = (value: number, rate: number) => {
  return value - (value * (rate / 100));
};

export const discountRest = (value: number, rate: number) => {
  return value * (rate / 100);
};
